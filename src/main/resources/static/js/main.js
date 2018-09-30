import Vue from 'vue'
import App from 'pages/App.vue'
import Login from 'pages/Login.vue'
import VueResource from 'vue-resource'

Vue.use(VueResource)


new Vue({
    el: '#application',
    render: a => a(App)
});

new Vue({
    el: '#login',
    render: a => a(Login)
});
/*
let measurementsApi = Vue.resource('/measurements/{id}');


Vue.component('page-header', {
    template: ''
});


Vue.component('measurement-item', {
    props: ['content'],
    template: '<li>{{content.type}}  {{content.registered}} {{content.value}} {{content.owner}}</li>'
});

Vue.component('measurement-add-dialog', {
    props: ['measurements'],
    data: function () {
        return {
            type: '',
            value: {}
        }
    },
    template: '<div><select v-model="type">' +
        '<option value="WATER" selected="selected">ВОДА</option>' +
        '<option value="ELECTRICITY">ЭЛЕКТРИЧЕСТВО</option>' +
        '<option value="HEATING">ТЕПЛО</option></select>' +
        '<input type="text" placeholder="Укажите значение" v-model="value">' +
        '<input type="button" value="Сохранить" @click="saveNewMeasurement"></div>',

    methods: {
        saveNewMeasurement : function () {
            let measurement = {type: this.type, value: this.value};
            measurementsApi.save({}, measurement).then(item => item.json().then(data => this.measurements.push(data)));
        }
    }
});


Vue.component('measurement-list', {
    props: ['measurements'],
    template: '<div><measurement-item v-for="measurement in measurements" :key="measurement.id" :content="measurement"/>' +
        '<measurement-add-dialog/></div>',
    created: function () {
        measurementsApi.get().then(resp =>
            resp.json().then(items =>
                items.forEach(item => this.measurements.push(item))
            ));
    }
});

var main = new Vue({
    el: '#application',
    template: '<measurement-list :measurements="measurements"></measurement-list>',
    data: {
        measurements:[]
    }
});
*/
