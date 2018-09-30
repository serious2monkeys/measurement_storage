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
            <tr v-for="entry in filteredData">
                <td v-for="key in columns">
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
                                {{entry['value'].hotWaterConsumption}}
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
            <tr>
                <td v-bind:colspan="columns.length">
                    <button class="btn btn-primary btn-lg btn-block" style="margin: 10px auto auto;
                display: block; background-color: #3386b9">
                        Добавить
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    export default {
        name: "Grid",
        props: {
            data: Array,
            columns: Array,
            filterKey: String
        },
        data:
            function () {
                var sortOrders = {};
                this.columns.forEach(function (key) {
                    sortOrders[key.name] = 1
                });
                return {
                    sortKey: '',
                    sortOrders: sortOrders
                }
            },
        computed:
            {
                filteredData: function () {
                    let sortKey = this.sortKey;
                    let filterKey = this.filterKey && this.filterKey.name.toLowerCase();
                    let order = this.sortOrders[sortKey.name] || 1;
                    let data = this.data;
                    if (filterKey) {
                        data = data.filter(function (row) {
                            return Object.keys(row).some(function (key) {
                                return String(row[key.name]).toLowerCase().indexOf(filterKey) > -1
                            })
                        })
                    }
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
            capitalize: function (str) {
                return str.charAt(0).toUpperCase() + str.slice(1)
            }
        },
        methods: {
            sortBy: function (key) {
                this.sortKey = key;
                this.sortOrders[key.name] = this.sortOrders[key.name] * -1
            }
        }
    }
</script>

<style scoped>

</style>