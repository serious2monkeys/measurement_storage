import Vue from 'vue'
import App from 'pages/App.vue'
import Login from 'pages/Login.vue'
import VueResource from 'vue-resource'
import Datetime from 'vue-datetime'
import 'vue-datetime/dist/vue-datetime.css'
import { Settings } from 'luxon'


Vue.use(VueResource)
Vue.use(Datetime)

Settings.defaultLocale = 'ru'

new Vue({
    el: '#application',
    render: a => a(App)
});

new Vue({
    el: '#login',
    render: a => a(Login)
});