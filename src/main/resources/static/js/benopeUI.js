var benopeUI = benopeUI || (function () {

    var DEFAULT_THROTTLING_MILLI = 1000;

    return {
        confirmModal: _.throttle(confirmModal, DEFAULT_THROTTLING_MILLI),
        createTimeOffModal: _.throttle(createTimeOffModal, DEFAULT_THROTTLING_MILLI)
    }

    function confirmModal(title, content, confirmCallback, cancelCallback) {
        var modal = $(getModalHtml({
            title: title,
            content: content,
            isCallbackExists: !!confirmCallback
        }));

        modal.find('.btn.btn-secondary').on('click', function () {
            closeModal();
            cancelCallback && cancelCallback()
        });

        modal.find('.btn.btn-primary').on('click', function () {
            closeModal();
            confirmCallback && confirmCallback();
        });

        $('body').append(modal.modal('show'));

        function getModalHtml(object) {
            return Handlebars.compile(
                "<div class='modal fade' tabindex='-1' role='dialog' aria-labelledby='updateModalLabel' aria-hidden='true'>" +
                "    <div class='modal-dialog' role='document'>" +
                "        <div class='modal-content'>" +
                "            <div class='modal-header'>" +
                "                <h5 class='modal-title' id='exampleModalLabel'>{{title}}</h5>" +
                "            </div>" +
                "            <div class='modal-body'>" +
                "               {{{content}}}" +
                "            </div>" +
                "            <div class='modal-footer'>" +
                "                <button type='button' class='btn btn-secondary'>닫기</button>" +
                "                {{#if isCallbackExists}}<button type='button' class='btn btn-primary'>확인</button>{{/if}}" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</div>"
            )(object);
        }
    }

    function createTimeOffModal(userId) {

        return new Promise(function (resolve) {
            var modal = $(getModalHtml());

            modal.find('.btn.btn-secondary').on('click', function () {
                closeModal();
            });

            modal.find('.btn.btn-primary').on('click', function () {
                closeModal();
                resolve({
                    userId: userId,
                    reason: modal.find('#reason').val(),
                    type: modal.find('#type').val(),
                    remainingDays: modal.find('#remainingDays').val(),
                    startDate: modal.find('#startDate').val(),
                    endDate: modal.find('#endDate').val()
                })
            });

            $('body').append(modal.modal('show'));
        });

        function getModalHtml() {
            var html =
                "<div class='modal fade' tabindex='-1' role='dialog' aria-labelledby='updateModalLabel' aria-hidden='true'>" +
                "    <div class='modal-dialog' role='document'>" +
                "        <div class='modal-content'>" +
                "            <div class='modal-header'>" +
                "                <h5 class='modal-title'>연차 등록</h5>" +
                "            </div>" +
                "            <div class='modal-body'>" +
                "               <form class='form' id='createTimeOffForm'>" +
                "                   <div class='form-group'>" +
                "                       <label for='reason'>사유</label>" +
                "                       <input type='text' class='form-control' placeholder='사유' id='reason' />" +
                "                   </div>" +
                "                   <div class='form-group'>" +
                "                       <label for='type'>유형</label>" +
                "                       <select class='form-control' id='type' name='type'>" +
                "                           <option value='PAID'>유급 휴가</option>" +
                "                       </select>" +
                "                   </div>" +
                "                   <div class='form-group'>" +
                "                       <label for='remainingDays'>잔여 일수</label>" +
                "                       <input type='number' class='form-control' placeholder='잔여 일수' id='remainingDays'/>" +
                "                   </div>" +
                "                   <div class='form-group'>" +
                "                       <label for='startDate'>시작일</label>" +
                "                       <input type='date' class='form-control' placeholder='시작일' id='startDate' />" +
                "                   </div>" +
                "                   <div class='form-group'>" +
                "                       <label for='endDate'>종료일</label>" +
                "                       <input type='date' class='form-control' placeholder='종료일' id='endDate' />" +
                "                   </div>" +
                "               </form>" +
                "            </div>" +
                "            <div class='modal-footer'>" +
                "                <button type='button' class='btn btn-secondary'>닫기</button>" +
                "                <button type='button' class='btn btn-primary'>등록</button>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</div>"
            return html
        }
    }

    function closeModal() {
        // TODO 예쁘게 닫을 수 있는 방법이 없을까...
        $('body').find('.modal, .modal-backdrop').remove()
        $('body').removeClass('modal-open')
    }
})();