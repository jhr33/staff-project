<template>
  <div class="login-container">

    <div class="login-form-box">
      <h2 style="font-size: 30px; color: #333">登 录</h2>
      <div style="margin: 10px 0 20px 0; color: #999">输入账号密码登录</div>
      <el-form :model="data.form" ref="formRef" :rules="data.rules">
        <el-form-item>
          <el-input size="large" :prefix-icon="User" placeholder="请输入账号" v-model="data.form.username"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input size="large" :prefix-icon="Lock" show-password placeholder="请输入密码" v-model="data.form.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select size="large" style="width: 100%" placeholder="请选择角色" v-model="data.form.role">
						<el-option value="admin" label="管理员"></el-option>
						<el-option value="employee" label="员工"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" class="login-form-btn" @click="login()">登 录</el-button>
        </el-form-item>
        <div>
          没有账号？请 <a type="text" style="color:#7366ff" href="/register">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>

  import { reactive, ref } from "vue";
  import { User, Lock } from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import router from "@/router";

  const data = reactive({
    form: {},
    rules: {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
    }
  })

  const formRef = ref()

  // 点击登录按钮的时候会触发这个方法
  const login = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // 调用后台的接口
        request.post('/login', data.form).then(res => {
          if (res.code === '200') {
            // 先设置用户菜单缓存
            localStorage.setItem('xm-user', JSON.stringify(res.data))
            ElMessage.success("登录成功")
            router.push('/manager/home')
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })).catch(error => {
      console.error(error)
    })
  }
</script>


<style scoped>
  .login-container {
    height: 100vh;
    overflow: hidden;
    /*background-image: url("@/assets/imgs/login_bg.jpg");*/
    background-color: #f8f8f8;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .login-form-box {
    width: 25%;
    padding: 30px;
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 0 20px rgba(89, 102, 122, 0.1);
  }

  .login-form-btn {
    width: 100%;
    padding: 10px;
    font-size: 16px;
    background-color: #7366ff !important;
    border-color: #7366ff !important;
  }

  h2 {
    margin: 0;
    padding: 0;
  }

  a {
    text-decoration: none;
    color: #5891aa;
  }

  .el-form-item {
    margin-bottom: 15px !important;
  }

</style>