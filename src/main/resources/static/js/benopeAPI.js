var benopeAPI = benopeAPI || (function ($) {

    return {
        getSession: function () {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/session",
                    method: "GET",
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

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
        },

        findTimeOffByUserId: function (userId) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/time-off/list",
                    method: "GET",
                    data: {
                        userId: userId
                    },
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        createTimeOff: function (createTimeOffRequest) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/time-off/",
                    method: "PUT",
                    contentType: "application/json",
                    acceptType: "json",
                    data: JSON.stringify(createTimeOffRequest),
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        deleteTimeOff: function (timeOffId) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/time-off/" + timeOffId,
                    method: "DELETE",
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        createLeaveRequest: function (request) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/leave-request",
                    method: "PUT",
                    contentType: "application/json",
                    acceptType: "json",
                    data: JSON.stringify(request),
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        findLeaveRequestByUserId: function (userId) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/leave-request/list",
                    method: "GET",
                    data: {
                        userId: userId
                    },
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        deleteLeaveRequest: function (leaveRequestId) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/leave-request/" + leaveRequestId,
                    method: "DELETE",
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        findLeaveRequestByApprovalUserId: function (approvalUserId) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/leave-request/approval/list",
                    method: "GET",
                    acceptType: "json",
                    data: {
                        approvalUserId: approvalUserId
                    },
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        approveLeaveRequest: function (leaveRequestId) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/leave-request/approval/" + leaveRequestId,
                    method: "POST",
                    contentType: "application/json",
                    acceptType: "json",
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },

        findApprovedLeaveRequest: function (startDate, endDate) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/leave-request/list/approved",
                    method: "GET",
                    acceptType: "json",
                    data: {
                        startDate: startDate.toISOString().split('T')[0],
                        endDate: endDate.toISOString().split('T')[0]
                    },
                    success: resolve,
                    error: _.partial(handleAjaxError, _, reject)
                });
            });
        },
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
