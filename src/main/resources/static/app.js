var stompClient = null;

function connect() {
    var socket = new SockJS('/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/ui-test', function (resp) {
            showTask(JSON.parse(resp.body));
        });
    });
}

function showTask(message) {
    $("#messages").append(`<tr><td>${message.content}</td><td>${message.timestamp}</td></tr>`);
}

$(function () {
    connect();
});
