<template>
    <div class="jumbotron">
        <table align="center" style="text-align: center">
            <thead>
            <tr>
                <th v-for="col in columns"
                    style="text-align: center"
                    @click="sortBy(col)"
                    :class="{ active: sortKey == col }">
                    {{ col.desc | capitalize }}
                    <span class="arrow" :class="sortOrders[col] > 0 ? 'asc' : 'dsc'"/>
                </th>
            </tr>
            </thead>
            <tbody>
            <template v-for="entry in filteredData">
                <tr @click="toggleRow(entry.id)" style="cursor: pointer">
                    <td v-for="key in columns" :class="{ opened: opened == entry.id }">
                    <span v-if="key.name == 'value'">
                        <div v-if="entry['type'] == 'WATER'" class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="basic-addon1">Горячая</span>
                            </div>
                            <div class="form-control" aria-describedby="basic-addon1">
                                {{entry['value'].hotWaterConsumption}}
                            </div>

                            <div class="input-group-prepend" style="margin-left: 5px">
                                <span class="input-group-text" id="basic-addon2">Холодная</span>
                            </div>
                             <div class="form-control" aria-describedby="basic-addon2">
                                {{entry['value'].coldWaterConsumption}}
                            </div>
                        </div>

                        <div v-else-if="entry['type'] == 'HEATING'" class="input-group" style="justify-content: center">
                            {{entry['value']}}
                        </div>

                         <div v-else class="input-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text" id="basic-addon3">День</span>
                            </div>
                            <div class="form-control" aria-describedby="basic-addon3">
                                {{entry['value'].dayCounter}}
                            </div>

                            <div class="input-group-prepend" style="margin-left: 5px">
                                <span class="input-group-text" id="basic-addon4">Ночь</span>
                            </div>
                             <div class="form-control" aria-describedby="basic-addon4">
                                {{entry['value'].nightCounter}}
                            </div>
                        </div>
                    </span>

                        <span v-else-if="key.name == 'type'">
                        <span v-if="entry['type'] == 'WATER'">
                            Вода
                        </span>
                        <span v-else-if="entry['type'] == 'ELECTRICITY'">
                            Электричество
                        </span>
                        <span v-else>
                            Отопление
                        </span>
                    </span>

                        <span v-else-if="key.name == 'registered'">
                        {{new Date(entry[key.name]).toLocaleString("ru")}}
                    </span>

                        <span v-else>
                        {{entry[key.name]}}
                    </span>
                    </td>
                </tr>
                <tr v-if="opened == entry.id">
                    <td v-bind:colspan="columns.length" style="background-color: bisque !important;">
                        <div class="input-group-prepend" id="button-addon3">

                            <button class="btn btn-primary btn-lg btn-block" style="margin: 5px;
                display: block; background-color: #38cf93; border-color: transparent" @click="editRow = entry">
                                Изменить
                            </button>

                            <button class="btn btn-primary btn-lg btn-block" style="margin: 5px;
                display: block; background-color: #cf3a45; border-color: transparent" @click="deleteRow(entry.id)">
                                Удалить
                            </button>
                        </div>
                    </td>
                </tr>
            </template>
            <tr>
                <td v-bind:colspan="columns.length">
                    <button class="btn btn-primary btn-lg btn-block" style="margin: 10px auto auto;
                display: block; background-color: #3386b9" @click="editRow = {}">
                        Добавить
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
        <MeasurementModal v-if="editRow" :rowData="editRow" @done="update"/>
    </div>
</template>

<script>
    import MeasurementModal from "./MeasurementModal.vue";

    export default {
        name: "Grid",
        components: {MeasurementModal},
        props: {
            data: Array,
            columns: Array,
            filterKey: String,
        },
        data:
            function () {
                let sortOrders = {};
                this.columns.forEach(function (key) {
                    sortOrders[key.name] = 1
                });
                return {
                    sortKey: '',
                    sortOrders: sortOrders,
                    opened: undefined,
                    editRow: undefined
                }
            },
        computed:
            {
                /**
                 * Формирование фильтрованного набора данных
                 * @returns {ArrayConstructor | *}
                 */
                filteredData: function () {
                    let sortKey = this.sortKey;
                    let order = this.sortOrders[sortKey.name] || 1;
                    let data = this.data;
                    if (sortKey) {
                        data = data.slice().sort(function (a, b) {
                            a = a[sortKey.name];
                            b = b[sortKey.name];
                            return (a === b ? 0 : a > b ? 1 : -1) * order
                        })
                    }
                    return data
                }
            },
        filters: {

            /**
             * Делаем первую букву заголовка заглавной
             * @param str - заголовок
             * @returns {string}
             */
            capitalize: function (str) {
                return str.charAt(0).toUpperCase() + str.slice(1)
            }
        },

        methods: {

            /**
             * Сортировка данных по ключу
             * @param key - ключ
             */
            sortBy: function (key) {
                this.sortKey = key;
                this.sortOrders[key.name] = this.sortOrders[key.name]
                    ? this.sortOrders[key.name] * -1
                    : 1;
            },

            /**
             * Активация контекстного меню
             * @param id - id строки
             */
            toggleRow: function (id) {
                if (this.opened === id) {
                    this.opened = undefined;
                } else {
                    this.opened = id;
                }
            },

            /**
             * Удаление строки
             * @param id -  id строки
             */
            deleteRow: function (id) {
                const index = this.data.map(elem => elem.id).indexOf(id);
                if (index > -1) {
                    this.$resource('/measurements{/id}').remove({id: id}).then(response => {
                        if (response.ok) {
                            this.data.splice(index, 1);
                        }
                    })
                }
            },

            /**
             * Обновление записи
             */
            update: function (row) {
                const index = this.data.map(elem => elem.id).indexOf(row.id);
                if (index > -1) {
                    this.data[index] = row;
                } else {
                    this.data.push(row);
                }
                this.editRow = undefined;
                this.opened = undefined;
            }
        }
    }
</script>

<style scoped>
    table {
        border: 2px solid #3386b9;
        border-radius: 3px;
        background-color: #fff;
        border-collapse: separate;
    }

    th {
        background-color: #3386b9;
        color: rgba(255, 255, 255, 0.66);
        cursor: pointer;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    td {
        background-color: #f9f9f9;
    }

    th, td {
        min-width: 120px;
        padding: 10px 20px;
    }

    th.active .arrow {
        opacity: 1;
    }

    .arrow {
        display: inline-block;
        vertical-align: middle;
        width: 0;
        height: 0;
        margin-left: 5px;
        opacity: 0.66;
    }

    .arrow.asc {
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        border-bottom: 4px solid #fff;
    }

    .arrow.dsc {
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        border-top: 4px solid #fff;
    }

    .opened {
        background-color: bisque !important;
    }
</style>