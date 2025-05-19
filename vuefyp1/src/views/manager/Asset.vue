<template>
  <div>
    
		<div class="card" style="margin-bottom: 5px">
			<el-input v-model="data.search.deviceName" prefix-icon="Search" style="width: 240px; margin-right: 10px; margin-bottom: 3px" placeholder="请输入设备名称"></el-input>
			<el-button type="info" plain @click="load">查询</el-button>
			<el-button type="warning" plain style="margin-right: 10px" @click="reset">重置</el-button>
		</div>
    <div v-if="data.user.role === 'admin'" class="card" style="margin-bottom: 5px">
			<el-button v-if="data.user.role === 'admin'" type="primary" plain @click="handleAdd">新增</el-button>
		</div>

    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip >

				<el-table-column label="设备名称" prop="deviceName"></el-table-column>
				<el-table-column label="价值" prop="price"></el-table-column>
				<el-table-column label="数量" prop="number"></el-table-column>
				<el-table-column label="故障情况" prop="situation"></el-table-column>
        <el-table-column label="闲置情况" prop="freeSit"></el-table-column>
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
				<el-form-item label="设备名称" prop="deviceName">
					<el-input v-model="data.form.deviceName" placeholder="请输入设备名称"></el-input>
				</el-form-item>
				<el-form-item label="价值" prop="price">
					<el-input v-model="data.form.price" placeholder="请输入价值"></el-input>
				</el-form-item>
				<el-form-item label="数量" prop="number">
					<el-input v-model="data.form.number" placeholder="请输入数量"></el-input>
				</el-form-item>
				<el-form-item label="故障情况" prop="situation">
					<el-input type="textarea" v-model="data.form.situation" placeholder="请输入故障情况"></el-input>
				</el-form-item>
				<el-form-item label="闲置情况" prop="freeSit">
					<el-input v-model="data.form.freeSit" placeholder="请输入闲置情况"></el-input>
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
const baseApi = 'asset'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  search: {},
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
	let price = data.form.price
	let priceRegNum = /^[0-9]*$/
	if (price && price !== '') {
		if (!priceRegNum.test(price)) {
			ElMessage.error("请输入正确的数字")
			return false
		}
	}

	let number = data.form.number
	let numberRegNum = /^[0-9]*$/
	if (number && number !== '') {
		if (!numberRegNum.test(number)) {
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



// 加载模块数据
load()

</script>
