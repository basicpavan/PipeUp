const sidebarShell = document.getElementById('sidebarShell');
const collapseButton = document.getElementById('collapseButton');

if (collapseButton && sidebarShell) {
    collapseButton.addEventListener('click', function () {
        sidebarShell.classList.toggle('collapsed');

        if (sidebarShell.classList.contains('collapsed')) {
            collapseButton.textContent = '»';
            collapseButton.setAttribute('aria-label', 'Expandir sidebar');
        } else {
            collapseButton.textContent = '«';
            collapseButton.setAttribute('aria-label', 'Recolher sidebar');
        }
    });
}

function toggleNovoEspacoForm() {
    const form = document.getElementById('novoEspacoForm');
    if (form) {
        form.classList.toggle('show');
    }
}

function abrirNovoEspacoParaEmpresa(button) {
    const empresaId = button.getAttribute('data-empresa-id');
    const form = document.getElementById('novoEspacoForm');

    if (!form) return;

    form.classList.add('show');

    const selectEmpresa = form.querySelector('select[name="empresaId"]');
    if (selectEmpresa && empresaId) {
        selectEmpresa.value = empresaId;
        selectEmpresa.dispatchEvent(new Event('change'));
    }

    const inputNome = form.querySelector('input[name="nome"]');
    if (inputNome) {
        inputNome.focus();
    }
}

function toggleEspacoMenu(button) {
    const currentMenu = button.nextElementSibling;

    document.querySelectorAll('.espaco-menu').forEach(menu => {
        if (menu !== currentMenu) {
            menu.classList.remove('show');
        }
    });

    if (currentMenu) {
        currentMenu.classList.toggle('show');
    }
}

function toggleEmpresaMenu(button) {
    const currentMenu = button.nextElementSibling;

    document.querySelectorAll('.empresa-menu').forEach(menu => {
        if (menu !== currentMenu) {
            menu.classList.remove('show');
        }
    });

    if (currentMenu) {
        currentMenu.classList.toggle('show');
    }
}

document.addEventListener('click', function (event) {
    const clickedInsideEspacoMenu = event.target.closest('.space-subitem-actions');
    const clickedInsideEmpresaMenu = event.target.closest('.empresa-menu-wrapper');

    if (!clickedInsideEspacoMenu) {
        document.querySelectorAll('.espaco-menu').forEach(menu => {
            menu.classList.remove('show');
        });
    }

    if (!clickedInsideEmpresaMenu) {
        document.querySelectorAll('.empresa-menu').forEach(menu => {
            menu.classList.remove('show');
        });
    }
});