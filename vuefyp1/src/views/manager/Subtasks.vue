<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-input v-model="data.search.belong" prefix-icon="Search" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入所属任务"></el-input>
			<el-button type="info" plain @click="load">查询</el-button>
			<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button>
		</div>
    <div v-if="data.user.role === 'admin'" class="card" style="margin-bottom: 5px">
			<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleAdd">新增</el-button>
			<el-button  v-if="data.user.role === 'admin'" type="danger" plain @click="delBatch">批量删除</el-button>
		</div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
				<el-table-column  v-if="data.user.role === 'admin'" type="selection" width="55" align="center"></el-table-column>
				<el-table-column label="子任务名" prop="subtasksName"></el-table-column>
				<el-table-column label="所属任务" prop="belong"></el-table-column>
				<el-table-column label="任务数量" prop="stNumber"></el-table-column>
				<el-table-column label="当前进度" prop="progress"></el-table-column>

        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope">
						<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
						<el-button v-if="data.user.role === 'admin'" type="danger" plain @click="del(scope.row.id)">删除</el-button>
            <el-button v-if="data.user.role === 'employee'" type="primary" plain @click="openSubmitDialog(scope.row)">提交</el-button>

          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="请填写信息" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="80px"  style="padding: 20px 30px">
				<el-form-item label="子任务名" prop="subtasksName" v-if="data.user.role === 'admin'">
					<el-input v-model="data.form.subtasksName" placeholder="请输入子任务名"></el-input>
				</el-form-item>
				<el-form-item label="所属任务" prop="belong" v-if="data.user.role === 'admin'">
					<el-select style="width: 100%" v-model="data.form.taskManagementId">
						<el-option v-for="item in data.taskManagementData" :value="item.id" :label="item.taskName" :key="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="任务数量" prop="stNumber" v-if="data.user.role === 'admin'">
					<el-input v-model="data.form.stNumber" placeholder="请输入任务数量"></el-input>
				</el-form-item>
				<el-form-item label="当前进度" prop="progress" v-if="data.user.role === 'admin'">
					<el-input v-model="data.form.progress" placeholder="请输入当前进度"></el-input>
				</el-form-item>
        <el-form-item label="提交数量" prop="number" required>
          <el-input
              v-model.number="data.form.number"
              placeholder="请输入本次提交数量"
              type="number"
              :max="data.form.stNumber - data.form.progress"
              :min="1"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" v-if="data.user.role === 'admin'"@click="save">保存</el-button>
            <el-button type="primary" v-if="data.user.role === 'employee'" @click="handleSubmit">确认</el-button>

        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
const baseApi = 'subtasks'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  search: {},
	ids: [],
	taskManagementData: [],
})



// 加载表格数据
const load = () => {

  data.search.pageNum = data.pageNum
  data.search.pageSize = data.pageSize
  request.get(baseApi + '/selectPage', {
    params: data.search
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total
  })
}
const submitData = {
  userid: data.user.id,
  taskid: data.form.id,
  ename: data.user.name,
  name: data.form.subtasksName,
  number: data.form.number, // 确保使用当前表单值
  status: "待审核",
  time: new Date().toISOString()
}




// 打开提交对话框
const openSubmitDialog = (row) => {
  // 完全重置表单数据
  data.form = {
    id: row.id,
    subtasksName: row.subtasksName,
    stNumber: row.stNumber,
    progress: row.progress,
    number: null // 确保每次打开都清空number
  }
  data.formVisible = true
}

// 提交处理
const handleSubmit = async () => {
  try {
    // 验证输入
    if (!data.form.number || data.form.number <= 0) {
      ElMessage.error('请输入有效的提交数量')
      return
    }

    // 检查是否超过剩余量
    const remaining = data.form.stNumber - data.form.progress
    if (data.form.number > remaining) {
      ElMessage.error(`提交数量不能超过剩余量 ${remaining}`)
      return
    }

    // 准备提交数据 - 创建全新对象
    const submitData = {
      userid: data.user.id,
      taskid: data.form.id,
      ename: data.user.name,
      name: data.form.subtasksName,
      number: data.form.number, // 确保使用当前表单值
      status: "待审核",
      time: new Date().toISOString()
    }

    const res = await request.post('/workrecord/add', submitData)

    if (res.code === '200') {
      ElMessage.success('提交成功，等待审核')
      data.formVisible = false
      load() // 刷新列表
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('提交失败: ' + error.message)
  }
}
// 打开新增弹窗
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

// 打开编辑弹窗
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 新增
const add = () => {
  request.post(baseApi + '/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新
const update = () => {
  request.put(baseApi + '/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success("更新成功")
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除
const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete(baseApi + '/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const save = () => {
	let stNumber = data.form.stNumber
	let stNumberRegNum = /^[0-9]*$/
	if (stNumber && stNumber !== '') {
		if (!stNumberRegNum.test(stNumber)) {
			ElMessage.error("请输入正确的数字")
			return false
		}
	}

	let progress = data.form.progress
	let progressRegNum = /^[0-9]*$/
	if (progress && progress !== '') {
		if (!progressRegNum.test(progress)) {
			ElMessage.error("请输入正确的数字")
			return false
		}
	}

  data.form.id ? update() : add()
}

const reset = () => {
  data.search = {}
  load()
}


// 批量删除表格数据
const delBatch = () => {
	if (data.ids.length === 0) {
		ElMessage.warning('请选择数据');
		return;
	}
	ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
		request.delete(baseApi + "/delete/batch", {data: data.ids}).then(res => {
			if (res.code === '200') {
				ElMessage.success('批量删除成功');
				load();
			} else {
				ElMessage.error(res.msg);
			}
		})
	}).catch(err => console.log(err))
}
const handleSelectionChange = (rows) => {
	data.ids = rows.map(v => v.id)
}
//提交

// 导出
const exp = () => {
	location.href = 'http://localhost:8080/subtasks/export?token=' + data.user.token
}
// 加载任务管理
const loadTaskManagement = () => {
	request.get("/taskManagement/selectAll").then(res => {
		if (res.code === '200') {
			data.taskManagementData = res.data;
		} else {
			ElMessage.error(res.msg);
		}
	})
}

// 加载模块数据
load()
// 加载任务管理
loadTaskManagement();

</script>
