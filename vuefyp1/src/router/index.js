import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login'},
    {
      path: '/manager',
      name: 'Manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/manager/login',
      children: [
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue')},
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue')},
		{ path: 'admin', meta: { name: '管理员' }, component: () => import("@/views/manager/Admin.vue") },
		{ path: 'employee', meta: { name: '员工' }, component: () => import("@/views/manager/Employee.vue") },
		{ path: 'taskManagement', meta: { name: '任务管理' }, component: () => import("@/views/manager/TaskManagement.vue") },
		{ path: 'subtasks', meta: { name: '子任务' }, component: () => import("@/views/manager/Subtasks.vue") },
		{ path: 'wages', meta: { name: '工资管理' }, component: () => import("@/views/manager/Wages.vue") },
		{ path: 'asset', meta: { name: '资产信息' }, component: () => import("@/views/manager/Asset.vue") },
		{ path: 'notice', meta: { name: '公告' }, component: () => import("@/views/manager/Notice.vue") },
		{ path: 'pAdmin', meta: { name: '个人信息' }, component: () => import("@/views/manager/pAdmin.vue")  },
		{ path: 'pEmployee', meta: { name: '个人信息' }, component: () => import("@/views/manager/pEmployee.vue")  },
        { path: 'qJRecord', meta: { name: '请假记录' }, component: () => import("@/views/manager/QJRecord.vue")  },
        { path: 'workRecord', meta: { name: '任务记录' }, component: () => import("@/views/manager/WorkRecord.vue")  },
          { path: 'overWork', meta: { name: '加班记录' }, component: () => import("@/views/manager/OverWork.vue")  },

      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue')},
	{ path: '/register', component: () => import("@/views/Register.vue") },
    { path: '/404', component: () => import('@/views/404.vue')},
    { path: '/:pathMatch(.*)', redirect: '/404', hidden: true }
  ]
})

export default router
