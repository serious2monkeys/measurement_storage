<template>
    <transition name="modal">
        <div class="modal-mask" style="min-width: 50%">
            <div class="modal-wrapper">
                <div class="modal-container">
                    <div class="modal-header">
                        <slot name="header">
                            Редактирование показания
                        </slot>
                    </div>

                    <div class="modal-body">
                        <slot name="body">
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text" id="pickerPic1">
                                        <img src="/assets/calendar-icon.png" width="16" height="16"/>
                                    </span>
                                </div>
                                <div class="form-control" aria-describedby="pickerPic" style="line-height: normal">
                                    <datetime type="datetime" id="picker" v-model="editData.registered"
                                              placeholder="Выберите дату"
                                              format="dd.MM.yyyy, HH:mm:ss"
                                              :phrases="{ok: 'Готово', cancel: 'Отмена'}"/>
                                </div>
                                <select class="custom-select mr-sm-2" v-model="editData.type" :disabled="rowData.type"
                                        id="inlineFormCustomSelectPref" style="line-height: normal"
                                        @change="switchType()">
                                    <option value="WATER">Вода</option>
                                    <option value="ELECTRICITY">Электричество</option>
                                    <option value="HEATING">Отопление</option>
                                </select>
                            </div>
                            <template>
                                <div v-if="editData.type == 'WATER'" class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1">Горячая</span>
                                    </div>
                                    <input type="text" v-model="editData['value'].hotWaterConsumption"
                                           class="form-control" aria-describedby="basic-addon1">

                                    <div class="input-group-prepend" style="margin-left: 5px">
                                        <span class="input-group-text" id="basic-addon2">Холодная</span>
                                    </div>
                                    <input class="form-control" v-model="editData['value'].coldWaterConsumption"
                                           aria-describedby="basic-addon2">
                                </div>

                                <div v-else-if="editData.type == 'ELECTRICITY'" class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon3">День</span>
                                    </div>
                                    <input type="text" v-model="editData['value'].dayCounter" class="form-control"
                                           aria-describedby="basic-addon3"/>

                                    <div class="input-group-prepend" style="margin-left: 5px">
                                        <span class="input-group-text" id="basic-addon4">Ночь</span>
                                    </div>
                                    <input type="text" v-model="editData['value'].nightCounter" class="form-control"
                                           aria-describedby="basic-addon4"/>
                                </div>

                                <div v-else-if="editData.type == 'HEATING'" class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon5">Значение счетчика</span>
                                    </div>
                                    <input type="text" v-model="editData['value']" class="form-control"
                                           aria-describedby="basic-addon5"/>
                                </div>
                            </template>
                        </slot>
                    </div>

                    <div class="modal-footer">
                        <slot name="footer">
                            <button class="btn btn-primary btn-lg btn-block" style="margin: 10px auto auto;
                display: block; background-color: #3386b9" @click="close">
                                Принять
                            </button>
                        </slot>
                    </div>
                </div>
            </div>
        </div>
    </transition>
</template>

<script>
    export default {
        name: "MeasurementModal",
        props: {
            showModal: Boolean,
            rowData: Object
        },
        methods: {
            close: function () {
                let action = this.editData.id
                    ? this.$resource('/measurements/').update(this.editData)
                    : this.$resource('/measurements').save(this.editData);
                action.then(response => response.json().then(result => {
                    this.editData = result;
                    this.$emit('done', result);
                }));
            },
            switchType: function () {
                switch (this.editData.type) {
                    case 'WATER':
                        this.editData.value = {
                            coldWaterConsumption: 0,
                            hotWaterConsumption: 0
                        };
                        break;
                    case 'ELECTRICITY':
                        this.editData.value = {
                            dayCounter: 0,
                            nightCounter: 0
                        };
                        break;
                    case 'HEATING':
                        this.editData.value = 0;
                        break;
                }
            }
        },
        data: function () {
            return {
                editData: {}
            }
        },
        created() {
            this.editData = this.rowData.type
                ? Object.assign({}, this.rowData)
                : {type: 'HEATING', value: 0};
        }
    }
</script>

<style scoped>
    .modal-mask {
        position: fixed;
        z-index: 9998;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, .5);
        display: table;
        transition: opacity .3s ease;
    }

    .modal-wrapper {
        display: table-cell;
        vertical-align: middle;
    }

    .modal-container {
        width: 30%;
        margin: 0px auto;
        padding: 20px 30px;
        background-color: #fff;
        border-radius: 2px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
        transition: all .3s ease;
        font-family: Helvetica, Arial, sans-serif;
    }

    .modal-header h3 {
        margin-top: 0;
        color: #42b983;
    }

    .modal-body {
        margin: 20px 0;
    }

    .modal-enter .modal-container,
    .modal-leave-active .modal-container {
        -webkit-transform: scale(1.1);
        transform: scale(1.1);
    }
</style>