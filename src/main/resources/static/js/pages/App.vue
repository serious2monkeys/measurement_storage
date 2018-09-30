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

    export default {
        name: "App",
        components: {Grid, Footer, Header},
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
                        this.gridColumns.push({name: 'id', desc: "Id"});
                        switch (response.role) {
                            case 'ADMIN':
                                this.gridColumns.push({name: 'owner', desc: 'Пользователь'});
                            case 'PLAIN_CUSTOMER':
                                this.gridColumns.push({name: 'type', desc: 'Тип'}, {name: 'registered', desc: 'Дата'}, {name: 'value', desc: 'Величина'});
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