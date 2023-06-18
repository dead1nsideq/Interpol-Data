$(document).ready(function() {
    var selectedLanguages = [];
    var selectedLanguagesList = $('#selectedLanguages');
    var addLanguageButton = $('#addLanguageButton');
    var newLanguageInput = $('#newLanguageInput');
    var selectedLanguagesInput = $('#selectedLanguagesInput');

    function addLanguage(language) {
        if (!selectedLanguages.includes(language)) {
            selectedLanguages.push(language);
            var listItem = $('<li>').text(language);
            var deleteButton = $('<button>').text('x').attr('class', 'remove-language');
            listItem.append(deleteButton);
            selectedLanguagesList.append(listItem);
            updateSelectedLanguagesInput();
        }
    }

    function removeLanguage(language) {
        var index = selectedLanguages.indexOf(language);
        if (index > -1) {
            selectedLanguages.splice(index, 1);
            updateSelectedLanguagesInput();
        }
    }

    function updateSelectedLanguagesInput() {
        selectedLanguagesInput.val(selectedLanguages.join(','));
    }

    $(document).on('click', '.remove-language', function() {
        var listItem = $(this).closest('li');
        var language = listItem.text().slice(0, -1); // Извлечение языка из элемента списка
        removeLanguage(language);
        listItem.remove();
    });

    addLanguageButton.click(function(event) {
        event.preventDefault(); // Предотвращение отправки формы
        var newLanguage = newLanguageInput.val();
        if (newLanguage && !selectedLanguages.includes(newLanguage)) {
            addLanguage(newLanguage);
            newLanguageInput.val('');
        }
    });


    newLanguageInput.keypress(function(event) {
        if (event.which === 13) {
            event.preventDefault();
            var newLanguage = newLanguageInput.val();
            if (newLanguage && !selectedLanguages.includes(newLanguage)) {
                addLanguage(newLanguage);
                newLanguageInput.val('');
            }
        }
    });

    var selectedLanguagesValue = /*[[${selectedLanguages}]]*/ '';
    if (selectedLanguagesValue) {
        selectedLanguages = selectedLanguagesValue.split(',');
        selectedLanguages.forEach(function(language) {
            addLanguage(language);
        });
    }

    $('#criminalForm').submit(function() {
        updateSelectedLanguagesInput();
        // Дополнительная логика перед отправкой формы
    });
});