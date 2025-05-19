<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-button v-if="data.user.role === 'employee'" type="primary" plain @click="handleAdd">提交加班申请</el-button>
    </div>

    <div  v-if="data.user.role === 'admin'" class="card"  style="margin-bottom: 5px">
      <el-button  v-if="data.user.role === 'admin'" type="danger" plain @click="delBatch">批量删除</el-button>
    </div>


    <div class="card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column label="加班人" prop="emname"></el-table-column>

        <el-table-column label="加班申请" prop="text"></el-table-column>


        <el-table-column label="审核状态" prop="status"></el-table-column>
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope" v-if="data.user.role === 'employee'" >
            <el-button :disabled="scope.row.status !=='待审核'" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button :disabled="scope.row.status !=='待审核'" type="danger" plain @click="del(scope.row.id)">撤销</el-button>
          </template>
          <template v-slot="scope" v-if="data.user.role === 'admin'" >
            <el-button :disabled="scope.row.status !=='待审核'" type="primary" plain @click="handleEdit(scope.row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="加班申请" v-model="data.formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef":model="data.form" :rules="data.rules" label-width="80px"  style="padding: 20px 30px">

        <el-form-item label="申请内容" prop="text"  v-if="data.user.role === 'employee'">
          <el-input type="textarea" v-model="data.form.text" placeholder="请输入正文"></el-input>
        </el-form-item>

        <el-form-item label="审核状态" prop="status"  v-if="data.user.role === 'admin'">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="待审核" value="待审核"></el-radio-button>
            <el-radio-button label="审核通过" value="审核通过"></el-radio-button>
            <el-radio-button label="审核拒绝" value="审核拒绝"></el-radio-button>

          </el-radio-group>

        </el-form-item>
        <el-form-item label="审核说明" prop="reason"  v-if="data.user.role === 'admin' ">
          <el-input  v-model="data.form.reason" autocomplete="off" placeholder="请输入备注 "></el-input>
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
import { reactive,ref } from "vue"
import request from "@/utils/request";
import {ElLoading, ElMessage, ElMessageBox} from "element-plus";
const baseApi = 'overwork'
const formRef=ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 10,  // 每页的个数
  formVisible: false,
  form: {
  },
  search: {},
  ids: [],
  rules:{
    time: [
      {
        required: true,
        message: '请选择请假时间',
        trigger: 'change',
        validator: (rule, value, callback) => {
          if (!value || value.length !== 2) {
            callback(new Error('请选择完整的请假时间段'));
          } else {
            callback();
          }
        }
      }
    ],

    text:[
      {
        required: true,message:'请输入加班申请',trigger:'blur'
      }
    ],
  }
})

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
};


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
  data.form.status='待审核'
  data.form.userid=data.user.id
  data.form.emname= data.user.name
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
      ElMessage.success('提交成功，等待管理员审核')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = async () => {
  try {
    const response = await request.put(baseApi + '/update', data.form);
    if (!response) {
      throw new Error('未收到服务器响应');
    }
    return response;
  } catch (error) {
    console.error('更新请求失败:', error);
    if (error.response) {
      const msg = error.response.data?.msg || error.response.statusText;
      throw new Error(msg || '服务器返回错误');
    } else if (error.request) {
      throw new Error('无法连接到服务器');
    } else {
      throw new Error('请求配置错误');
    }
  }
}
// 更新
/*const update = () => {
  request.put(baseApi + '/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

 */

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
const save = async () => {
  try {
    await formRef.value.validate();

    // 处理时间数据
    if (data.form.time && data.form.time.length === 2) {
      data.form.starttime = data.form.time[0];
      data.form.endtime = data.form.time[1];
      data.form.time= data.form.startime + " ~ " + data.form.endtime;
    }

    const loading = ElLoading.service({
      lock: true,
      text: '保存中...',
      background: 'rgba(0, 0, 0, 0.7)'
    });

    try {
      const res = await request({
        url: data.form.id ? `${baseApi}/update` : `${baseApi}/add`,
        method: data.form.id ? 'put' : 'post',
        data: data.form
      });

      if (res.code === '200') {
        ElMessage.success(data.form.id ? '更新成功' : '提交成功');
        data.formVisible = false;
        load();
      } else {
        ElMessage.error(res.msg || (data.form.id ? '更新失败' : '提交失败'));
      }
    } catch (error) {
      console.error('保存错误:', error);
      ElMessage.error(error.response?.data?.msg || error.message || '操作失败');
    } finally {
      loading.close();
    }
  } catch (validationError) {
    console.log('表单验证未通过', validationError);
  }
}
/*
const save = async () => {
  try {
    // 1. 表单验证
    const valid = await formRef.value.validate();
    if (!valid) return;

    // 2. 准备请求
    const loading = ElLoading.service({
      lock: true,
      text: '保存中...',
      background: 'rgba(0, 0, 0, 0.7)'
    });

    const isEdit = !!data.form.id;
    const url = isEdit ? `${baseApi}/update` : `${baseApi}/add`;
    const method = isEdit ? 'put' : 'post';
    const successMsg = isEdit ? '更新成功' : '提交成功，等待管理员审核';

    try {
      // 3. 发送请求
      const response = await request({ url, method, data: data.form });

      // 4. 处理响应
      if (!response) {
        throw new Error('未收到服务器响应');
      }

      if (response.code === '200') {
        ElMessage.success(successMsg);
        data.formVisible = false;
        load(); // 刷新数据
      } else {
        throw new Error(response.msg || (isEdit ? '更新失败' : '提交失败'));
      }
    } catch (error) {
      console.error('保存错误:', error);
      const errorMsg = error.response?.data?.msg ||
          error.message ||
          '操作失败，请检查网络或联系管理员';
      ElMessage.error(errorMsg);
    } finally {
      loading.close();
    }
  } catch (validationError) {
    console.log('表单验证未通过', validationError);
  }
}


 */


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
  location.href = 'http://localhost:8080/overwork/export?token=' + data.user.token
}

// 加载模块数据
load()

</script>
