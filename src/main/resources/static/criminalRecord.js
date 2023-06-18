$(document).ready(function() {
    var selectedCriminalRecords = [];
    var selectedCriminalRecordsList = $('#selectedCriminalRecords');
    var addCriminalRecordButton = $('#addCriminalRecordButton');
    var newCriminalRecordInput = $('#newCriminalRecordInput');
    var selectedCriminalRecordsInput = $('#selectedCriminalRecordsInput');

    function addCriminalRecord(criminalRecord) {
        if (!selectedCriminalRecords.includes(criminalRecord)) {
            selectedCriminalRecords.push(criminalRecord);
            var listItem = $('<li>').text(criminalRecord);
            var deleteButton = $('<button>').text('x').attr('class', 'remove-criminalRecord');
            listItem.append(deleteButton);
            selectedCriminalRecordsList.append(listItem);
            updateSelectedCriminalRecordsInput();
        }
    }

    function removeCriminalRecord(criminalRecord) {
        var index = selectedCriminalRecords.indexOf(criminalRecord);
        if (index > -1) {
            selectedCriminalRecords.splice(index, 1);
            updateSelectedCriminalRecordsInput();
        }
    }

    function updateSelectedCriminalRecordsInput() {
        selectedCriminalRecordsInput.val(selectedCriminalRecords.join(','));
    }

    $(document).on('click', '.remove-criminalRecord', function() {
        var listItem = $(this).closest('li');
        var criminalRecord = listItem.text().slice(0, -1); // Извлечение языка из элемента списка
        removeCriminalRecord(criminalRecord)
        listItem.remove();
    });

    addCriminalRecordButton.click(function(event) {
        event.preventDefault(); // Предотвращение отправки формы
        var newCriminalRecord = newCriminalRecordInput.val();
        if (newCriminalRecord && !selectedCriminalRecords.includes(newCriminalRecord)) {
            addCriminalRecord(newCriminalRecord);
            newCriminalRecordInput.val('');
        }
    });


    newCriminalRecordInput.keypress(function(event) {
        if (event.which === 13) {
            event.preventDefault();
            var newCriminalRecord = newCriminalRecordInput.val();
            if (newCriminalRecord && !selectedCriminalRecords.includes(newCriminalRecord)) {
                addCriminalRecord(newCriminalRecord);
                newCriminalRecordInput.val('');
            }
        }
    });

    var selectedCriminalRecordsValue = /*[[${selectedCriminalRecords}]]*/ '';
    if (selectedCriminalRecordsValue) {
        selectedCriminalRecords = selectedCriminalRecordsValue.split(',');
        selectedCriminalRecords.forEach(function(criminalRecord) {
            addCriminalRecord(criminalRecord);
        });
    }

    $('#criminalForm').submit(function() {
        updateSelectedCriminalRecordsInput()
        // Дополнительная логика перед отправкой формы
    });
});