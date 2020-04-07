<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <article>
        
    </article>
    <input type="button" value="fetch" onclick="
    fetch('predict').then(function(response){
        response.text().then(function(text){
            document.querySelector('article').innerHTML = text;
        })
    })
    ">
</body>
</html>