<template>
    <div class="text-center">
        <form class="form-signin" action="/login" method="post">
            <img class="mb-4" src="assets/enter-image.png" alt="" width="72" height="72">
            <h1 class="h3 mb-3 font-weight-normal">Введите логин и пароль</h1>
            <fieldset>
                <label for="login" class="sr-only">Логин</label>
                <input type="text" id="login" class="form-control" name="login" placeholder="Ваш логин"
                       required="required"
                       autofocus="" onchange="this.setAttribute('value', this.value);">
            </fieldset>
            <fieldset>
                <label for="password" class="sr-only">Пароль</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Пароль"
                       required="required"
                       onchange="this.setAttribute('value', this.value);">
            </fieldset>
            <div class="errorText" style="visibility: hidden">Неверный логин или пароль</div>
            <fieldset>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
            </fieldset>
        </form>
        <p class="mt-5 mb-3 text-muted">© 2018-Forever</p>
    </div>
</template>

<script>
    export default {
        name: "Login",
        created: function () {
            window.onload = function () {
                let location = document.location.href;

                if (location.indexOf('?error') != -1) {
                    document.querySelector('.errorText').style.visibility = '';
                }
                let loginInput = document.querySelector('input[name="login"]');
                if (loginInput) {
                    loginInput.focus()
                }
            }
        }
    }
</script>

<style scoped>

</style>