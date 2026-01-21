let URL = "http://localhost:8080/users"

async function loadAdminPage() {
    await loadData()
    await fillRolesSelector()
    document.getElementById("authorizedUser").innerText = await fetchCurrentUser();
    const label = document.getElementById('header')
    const tabContainer = document.querySelector('.nav.nav-tabs');
    tabContainer.style.display = 'flex'
    label.innerText = "Admin page"
    document.getElementById('h').innerText = "All users"
}

async function loadUserPage() {
    document.getElementById("authorizedUser").innerText = await fetchCurrentUser()
    await loadUserData()
    const label = document.getElementById('header')
    const tabContainer = document.querySelector('.nav.nav-tabs');
    tabContainer.style.display = 'none'
    label.innerText = "User information-page"
    document.getElementById('h').innerText = "About user"
}

async function loadData() {
    fetch(URL)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then(async data => {
            await updateTableData1(data);
        })
        .catch(error => {
            console.error('Произошла ошибка при запросе:', error);
        });
}
async function loadUserData() {
    fetch("/users/current")
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then(async data => {
            await updateTableData2(data);
        })
        .catch(error => {
            console.error('Произошла ошибка при запросе:', error);
        });
}

async function fetchCurrentUser() {
    try {
        const response = await fetch('users/current');
        if (response.ok) {
            const user = await response.json();
            return user.username + " with roles: " + user.rolesToString;
        } else {
            console.log('Не удалось получить данные пользователя. Статус:', response.status);
        }
    } catch (error) {
        console.error('Ошибка при запросе пользователя:', error);
    }
}

function updateTableData1(data) {
    let table = document.getElementById('adminTable')
    let table2 = document.getElementById('userTable')
    table.style.display = 'block';
    table2.style.display = 'none';
    if (!table) {
        console.error("Таблица с id 'userTable' не найдена.");
        return;
    }
    const tableBody = document.getElementById('admintbody')

    if (!tableBody) {
        console.error("Элемент <tbody> не найден внутри таблицы.");
        return;
    }
    while (tableBody.firstChild) {
        tableBody.removeChild(tableBody.firstChild);
    }
    data.forEach(user => {
        const row = tableBody.insertRow(); // Добавляем строку в tbody
        row.innerHTML = `
      <td>${user.id}</td>
      <td>${user.firstName}</td>
      <td>${user.lastName}</td>
      <td>${user.age}</td>
      <td>${user.username}</td>
      <td>${user.rolesToString}</td>
      <td><button class="edit-button btn btn-info text-white" data-id="${user.id}">Edit</button></td>
      <td><button class="delete-button btn btn-danger text-white" data-id="${user.id}">Delete</button></td>
    `;
        const btn = document.getElementById('myForm1');
        const editButton = row.querySelector('.edit-button');
        const myModal = new bootstrap.Modal(document.getElementById('myModal'));
        let id;
        editButton.addEventListener('click', () => {
            myModal.show();
            id = editButton.getAttribute('data-id')
            unlockData()
            document.getElementById('action').innerText = 'Edit';
            document.getElementById('exampleModalLabel').innerText = "Edit user"
            loadUser(id)
            btn.addEventListener('submit', async function (e) {
                e.preventDefault();
                if (id) {
                    let formData = collectingUserData(id)
                    fetch(URL, {
                        method: 'PUT',
                        body: formData
                    })
                        .then(response => {
                            if (response.ok) {
                                loadData();
                                $('#myModal').modal('hide')
                                hideTab2AndSwitchToTab1();
                            }
                        })
                        .catch((error) => {
                            window.alert(error.toString())
                        });
                }
            });

        });
        const deleteButton = row.querySelector('.delete-button');
        deleteButton.addEventListener('click', () => {
            myModal.show();
            id = deleteButton.getAttribute('data-id')
            lockData()
            document.getElementById('action').innerText = "Delete";
            document.getElementById('exampleModalLabel').innerText = "Delete user"
            loadUser(id)
            btn.addEventListener('submit', async function (e) {
                e.preventDefault();
                if (id) {
                    fetch('/users/' + id, {
                        method: 'DELETE'
                    })
                        .then(response => {
                            if (response.ok) {
                                loadData();
                                $('#myModal').modal('hide')
                                hideTab2AndSwitchToTab1();
                            }
                        })
                        .catch((error) => {
                            window.alert(error.toString())
                        });
                }
            });

        })
    });
}


function updateTableData2(data) {
    let table = document.getElementById('userTable')
    let table2 = document.getElementById('adminTable')
    table.style.display = 'block';
    table2.style.display = 'none';
    if (!table) {
        console.error("Таблица с id 'userTable' не найдена.");
        return;
    }
    const tableBody = document.getElementById('usertbody')
    if (!tableBody) {
        console.error("Элемент <tbody> не найден внутри таблицы.");
        return;
    }
    while (tableBody.firstChild) {
        tableBody.removeChild(tableBody.firstChild);
    }

    const row = tableBody.insertRow();
    row.innerHTML = `
        <td>${data.id}</td>
        <td>${data.firstName}</td>
        <td>${data.lastName}</td>
        <td>${data.age}</td>
        <td>${data.username}</td>
        <td>${data.rolesToString}</td>
    `;

}

function submitForm(event) {
    event.preventDefault();
    const form = document.getElementById('createForm');
    const formData = new FormData();
    formData.append("firstName", document.getElementById('firstName').value)
    formData.append("lastName", document.getElementById('lastName').value)
    formData.append("age", document.getElementById('age').value)
    formData.append("username", document.getElementById('username').value)
    formData.append("password", document.getElementById('password').value)
    const roleSelect = document.getElementById('roles');

    const selectedOptions = [];
    for (let i = 0; i < roleSelect.options.length; i++) {
        if (roleSelect.options[i].selected) {
            selectedOptions.push(roleSelect.options[i].value);
        }
    }
    formData.append('roles', selectedOptions);

    fetch("/users", {
        method: 'POST',  // Указываем метод POST
        body: formData, // Передаем данные формы
    })
        .then(async response => {
            if (response.ok) {
                await loadData()
                hideTab2AndSwitchToTab1()
                form.reset();
            } else {
                console.error('Error adding user:', response.status);
                response.text().then(errorMessage => {
                    console.error('Server error message:', errorMessage);
                });
            }
        })
        .catch(error => {
            alert(error.toString())
        });
}

async function fillRolesSelector() {
    try {
        const response = await fetch('/roles'); // Выполняем GET-запрос к /roles
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const selectElement = document.getElementById('roles'); // Находим селектор по его ID
        selectElement.innerHTML = '';
        const selectElement1 = document.getElementById('roles1'); // Находим селектор по его ID
        selectElement1.innerHTML = '';
        const roles = await response.json();

        roles.forEach(role => {
            const option1 = document.createElement('option');  // Создаем option для selectElement
            option1.value = role.id;
            option1.text = role.name;
            selectElement.appendChild(option1);

            const option2 = document.createElement('option');  // Создаем option для selectElement1
            option2.value = role.id;
            option2.text = role.name;
            selectElement1.appendChild(option2);
        });

    } catch (error) {
        console.error('Ошибка при получении или обработке списка ролей:', error);
    }
}


function hideTab2AndSwitchToTab1() {
    $('#tab1-tab').tab('show'); // Переключаемся на первую вкладку
}

function loadUser(id) {
    let getURL = '/users/' + id;
    fetch(getURL, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Сетевая ошибка: ' + response.status);
            }
            return response.text();
        })
        .then(data => {
            const users = JSON.parse(data);
            document.getElementById('Id1').value = id;
            document.getElementById('firstName1').value = users.firstName;
            document.getElementById('lastName1').value = users.lastName;
            document.getElementById('age1').value = users.age;
            document.getElementById('username1').value = users.username;
            document.getElementById('password1').value = users.password;
        })
        .catch(error => {
            console.error('Ошибка при запросе:', error); // Обработка ошибок
        });
}

function collectingUserData(id) {
    const formData = new FormData();
    formData.append('id', id);
    formData.append('username', document.getElementById('username1').value);
    formData.append('firstName', document.getElementById('firstName1').value);
    formData.append('lastName', document.getElementById('lastName1').value);
    formData.append('password', document.getElementById('password1').value);
    formData.append('age', document.getElementById('age1').value);
    const roleSelect = document.getElementById('roles1');

    const selectedOptions = [];
    for (let i = 0; i < roleSelect.options.length; i++) {
        if (roleSelect.options[i].selected) {
            selectedOptions.push(roleSelect.options[i].value);
        }
    }
    formData.append('roles', selectedOptions);
    return formData;
}

function unlockData() {
    document.getElementById('firstName1').disabled = false;
    document.getElementById('lastName1').disabled = false;
    document.getElementById('age1').disabled = false;
    document.getElementById('username1').disabled = false;
    document.getElementById('password1').disabled = false;
    document.getElementById('roles1').disabled = false;
}

function lockData() {
    document.getElementById('firstName1').disabled = true;
    document.getElementById('lastName1').disabled = true;
    document.getElementById('age1').disabled = true;
    document.getElementById('username1').disabled = true;
    document.getElementById('password1').disabled = true;
    document.getElementById('roles1').disabled = true;
}


const closeButton = document.querySelectorAll('.btn-secondary')
closeButton.forEach(closeButton => {
    closeButton.addEventListener('click', function (e) {
        $('#myModal').modal('hide')
    })
})
