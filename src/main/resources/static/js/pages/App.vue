<template>
    <div>
        <Header/>
        <Grid :data="measurements" :columns="gridColumns" :filter-key="searchQuery"/>
        <Footer/>
    </div>
</template>

<script>
    import Header from "../components/Header.vue";
    import Footer from "../components/Footer.vue";
    import Grid from "../components/Grid.vue";
    import Datetime from 'vue-datetime';

    export default {
        name: "App",
        components: {Grid, Footer, Header, Datetime},
        data() {
            return {
                measurements: [],
                searchQuery: '',
                gridColumns: []
            };
        },
        beforeCreate() {
            this.$resource("/role").get().then(roleValue => {
                if (roleValue.ok) {
                    roleValue.json().then(response => {
                        this.gridColumns.push({name: 'registered', desc: 'Дата'});
                        switch (response.role) {
                            case 'ADMIN':
                                this.gridColumns.push({name: 'owner', desc: 'Пользователь'});
                            case 'PLAIN_CUSTOMER':
                                this.gridColumns.push({name: 'type', desc: 'Тип'}, {name: 'value', desc: 'Величина'});
                                break;
                        }

                        this.$resource("/measurements").get().then(value => {
                            if (value.ok) {
                                value.json().then(result => result.forEach(element => {
                                    this.measurements.push(element);
                                }));
                            }
                        });
                    })
                }
            });

        }
    }
</script>

<style scoped>
</style>