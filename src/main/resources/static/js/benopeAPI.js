var benopeAPI = benopeAPI || (function ($) {

    return {
        findUserList: function (pageable) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    url: "/api/v1/user/user-list",
                    method: "GET",
                    contentType: "application/json",
                    acceptType: "json",
                    data: JSON.stringify(pageable),
                    success: resolve,
                    error: reject
                });
            })
        }
    }
})(jQuery)