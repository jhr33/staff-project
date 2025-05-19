<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-input v-model="data.search.taskName" prefix-icon="Search" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入任务名"></el-input>
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
				<el-table-column label="任务名" prop="taskName"></el-table-column>
				<el-table-column label="任务总量" prop="taskNumber"></el-table-column>
        <el-table-column label="样衣" prop="clothImg" >
					<template v-slot="scope">
						<el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="scope.row.clothImg" :src="scope.row.clothImg" :preview-src-list="[scope.row.clothImg]" preview-teleported></el-image>
					</template>
				</el-table-column>
				<el-table-column label="交付时限" prop="ddl"></el-table-column>
				<el-table-column label="任务信息" prop="taskInformation"></el-table-column>
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope">
						<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
						<el-button v-if="data.user.role === 'admin'" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="请填写信息" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="80px"  style="padding: 20px 30px">
				<el-form-item label="任务名" prop="taskName">
					<el-input v-model="data.form.taskName" placeholder="请输入任务名"></el-input>
				</el-form-item>
				<el-form-item label="任务总量" prop="taskNumber">
					<el-input v-model="data.form.taskNumber" placeholder="请输入任务总量"></el-input>
				</el-form-item>
				<el-form-item label="样衣" prop="clothImg">
					<el-upload class="avatar-uploader" :action="'http://localhost:8080/files/upload'" :on-success="clothImgSuccessUpload" list-type="picture">
						<el-button type="primary">点击上传</el-button>
					</el-upload>
				</el-form-item>
				<el-form-item label="交付时限" prop="ddl">
					<el-date-picker v-model="data.form.ddlSlot" type="daterange" value-format="YYYY-MM-DD" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 100%"></el-date-picker>
				</el-form-item>
				<el-form-item label="任务信息" prop="taskInformation">
					<el-input type="textarea" v-model="data.form.taskInformation" placeholder="请输入任务信息"></el-input>
				</el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
const baseApi = 'taskManagement'

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
	let taskNumber = data.form.taskNumber
	let taskNumberRegNum = /^[0-9]*$/
	if (taskNumber && taskNumber !== '') {
		if (!taskNumberRegNum.test(taskNumber)) {
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
// 导出
const exp = () => {
	location.href = 'http://localhost:8080/taskManagement/export?token=' + data.user.token
}
const clothImgSuccessUpload = (res) => {
	data.form.clothImg = res.data;
}

// 加载模块数据
load()

</script>
