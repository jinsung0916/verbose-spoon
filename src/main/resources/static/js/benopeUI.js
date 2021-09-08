var benopeUI = benopeUI || (function () {

    var DEFAULT_THROTTLING_MILLI = 1000;

    return {
        confirmModal: _.throttle(confirmModal, DEFAULT_THROTTLING_MILLI)
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

    function closeModal() {
        // TODO 예쁘게 닫을 수 있는 방법이 없을까...
        $('body').find('.modal, .modal-backdrop').remove()
        $('body').removeClass('modal-open')
    }
})();