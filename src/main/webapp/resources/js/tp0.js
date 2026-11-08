// Fait défiler la zone de conversation vers le bas après chaque chargement.
window.addEventListener('load', function () {
    var zone = document.getElementById('form:conversation');
    if (zone) { zone.scrollTop = zone.scrollHeight; }
});
