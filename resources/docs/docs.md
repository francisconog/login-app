<h1 class="title">Parabéns, jovem, você se conectou ao login-app!</h1>

Nosso site tem como único propósito fazer você fazer login e nada mais

<p class="title is-5">Quer fazer login de novo?, clique no botão abaixo pra se desconectar</p>
{% csrf-field %}
<form method="POST" action="/api/logout">
<button type="submit" class="btn btn-primary btn">Aqui</button>
</form>