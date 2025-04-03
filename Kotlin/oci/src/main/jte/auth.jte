@param String username
@param java.util.Map<?, ?> security

<!DOCTYPE html>
<html lang="en">
<head>
    <title>GDK - OpenID Connect</title>
</head>
<body>
<h1>GDK - OpenID Connect</h1>

<h2>username: <span>${username}</span></h2>

<nav>
    <ul>
    @if(security == null)
        <li><a href="/oauth/login/gdk">Enter</a></li>
    @else
        <li><a href="/logout">Logout</a></li>
    @endif
    </ul>
</nav>
</body>
</html>
