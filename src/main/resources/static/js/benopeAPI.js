var benopeAPI = benopeAPI || (function ($) {

    return {
        findUserList: function (page = 0, size = 100) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/user/list",
                    method: "GET",
                    acceptType: "json",
                    data: {
                        page: page,
                        size: size
                    },
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        updateUser: function (username, updateUserRequest) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/user/" + username,
                    method: "POST",
                    contentType: "application/json",
                    acceptType: "json",
                    data: JSON.stringify(updateUserRequest),
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        createUser: function (createUserRequest) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/user/",
                    method: "PUT",
                    contentType: "application/json",
                    acceptType: "json",
                    data: JSON.stringify(createUserRequest),
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        deleteUser: function (username) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/user/" + username,
                    method: "DELETE",
                    contentType: "application/json",
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        updateUserPassword: function (username, updateUserPasswordRequest) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/user/" + username + "/password",
                    method: "POST",
                    contentType: "application/json",
                    acceptType: "json",
                    data: JSON.stringify(updateUserPasswordRequest),
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        resetUserPassword: function (username) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/user/" + username + "/password",
                    method: "DELETE",
                    contentType: "application/json",
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        }
    }

    function handleAjaxError(xhr, reject) {
        var status = String(_.get(xhr, 'responseJSON.status') || "");
        var message = _.get(xhr, 'responseJSON.message');
        if (status === '401') {
            // Unauthorized(세션 만료)
            location.href = "/login"
        } else if (status === '403') {
            reject(new Error('권한이 없습니다.'));
        } else if (status.startsWith('4')) {
            // Client error -> Delegate error handling to client
            reject(new Error(parseErrorMessage(message)));
        } else {
            // Server error -> Throw error
            throw new Error(message);
        }
    }

    function parseErrorMessage(message) {
        try {
            var json = JSON.parse(message);
            if (json instanceof Array) {
                return json.join('<br/>');
            } else {
                return message;
            }
        } catch (e) {
            return message;
        }
    }

})(jQuery)
