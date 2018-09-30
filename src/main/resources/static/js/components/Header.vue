<template>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand" href="#">Счетчик.PRO</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
                aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto"/>
            <a href="logout">
                <button class="btn btn-outline-success my-2 my-sm-0" type="button">Выход</button>
            </a>

        </div>
    </nav>
</template>

<script>
    export default {
        name: "Header"
    }
</script>

<style scoped>

</style>