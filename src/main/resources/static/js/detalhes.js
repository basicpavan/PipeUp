// ── Funcionalidades da tela de detalhes ──

document.addEventListener('DOMContentLoaded', function() {
    
    // ── Auto-save do formulário ──
    initAutoSave();
    
    // ── Validação em tempo real ──
    initValidation();
    
    // ── Atalhos de teclado ──
    initKeyboardShortcuts();
    
    // ── Confirmação antes de sair ──
    initUnsavedChangesWarning();
    
    // ── Melhorias no calendário ──
    initCalendarEnhancements();
});

// ── Auto-save ──
let autoSaveTimeout;
let hasUnsavedChanges = false;

function initAutoSave() {
    const form = document.querySelector('.col-esq form');
    if (!form) return;
    
    const inputs = form.querySelectorAll('input, select, textarea');
    
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            hasUnsavedChanges = true;
            clearTimeout(autoSaveTimeout);
            
            // Auto-save após 2 segundos de inatividade
            autoSaveTimeout = setTimeout(() => {
                saveForm();
            }, 2000);
        });
    });
}

function saveForm() {
    const form = document.querySelector('.col-esq form');
    if (!form) return;
    
    const formData = new FormData(form);
    
    fetch(form.action, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            hasUnsavedChanges = false;
            showNotification('Alterações salvas automaticamente', 'success');
        } else {
            showNotification('Erro ao salvar automaticamente', 'error');
            console.error('Erro no auto-save:', response.status, response.statusText);
        }
    })
    .catch(error => {
        console.error('Erro no auto-save:', error);
        showNotification('Erro de rede no auto-save', 'error');
    });
}

// ── Validação em tempo real ──
function initValidation() {
    const tituloInput = document.querySelector('input[name="titulo"]');
    const dataInicioInput = document.querySelector('input[name="dataInicio"]');
    const dataEntregaInput = document.querySelector('input[name="dataEntrega"]');
    
    if (tituloInput) {
        tituloInput.addEventListener('input', function() {
            validateTitulo(this);
        });
    }
    
    if (dataInicioInput && dataEntregaInput) {
        [dataInicioInput, dataEntregaInput].forEach(input => {
            input.addEventListener('change', function() {
                validateDates(dataInicioInput, dataEntregaInput);
            });
        });
    }
}

function validateTitulo(input) {
    const value = input.value.trim();
    const maxLength = 100;
    
    if (value.length === 0) {
        showFieldError(input, 'Título é obrigatório');
    } else if (value.length > maxLength) {
        showFieldError(input, `Máximo ${maxLength} caracteres`);
    } else {
        clearFieldError(input);
    }
}

function validateDates(inicioInput, entregaInput) {
    const inicio = new Date(inicioInput.value);
    const entrega = new Date(entregaInput.value);
    
    if (inicioInput.value && entregaInput.value && entrega < inicio) {
        showFieldError(entregaInput, 'Data de entrega deve ser posterior à data de início');
    } else {
        clearFieldError(entregaInput);
    }
}

function showFieldError(input, message) {
    clearFieldError(input);
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    errorDiv.style.cssText = `
        color: #ff4d4d;
        font-size: 12px;
        margin-top: 4px;
        margin-left: 14px;
    `;
    
    input.parentNode.insertBefore(errorDiv, input.nextSibling);
    input.style.borderColor = '#ff4d4d';
}

function clearFieldError(input) {
    const error = input.parentNode.querySelector('.field-error');
    if (error) {
        error.remove();
    }
    input.style.borderColor = '';
}

// ── Atalhos de teclado ──
function initKeyboardShortcuts() {
    document.addEventListener('keydown', function(e) {
        // Ctrl + S = Salvar
        if (e.ctrlKey && e.key === 's') {
            e.preventDefault();
            const saveBtn = document.querySelector('.btn-salvar');
            if (saveBtn) {
                saveBtn.click();
            } else {
                console.warn('Atalho Ctrl+S: Botão "Salvar" não encontrado.');
            }
        }
        
        // Ctrl + Enter = Adicionar atividade
        if (e.ctrlKey && e.key === 'Enter') {
            e.preventDefault();
            const addBtn = document.querySelector('.btn-add');
            if (addBtn) {
                addBtn.click();
            }
        }
        
        // ESC = Voltar ao dashboard
        if (e.key === 'Escape') {
            window.location.href = '/dashboard';
        }
    });
}

// ── Aviso de alterações não salvas ──
function initUnsavedChangesWarning() {
    window.addEventListener('beforeunload', function(e) {
        if (hasUnsavedChanges) {
            e.preventDefault();
            e.returnValue = '';
        }
    });
}

// ── Melhorias no calendário ──
function initCalendarEnhancements() {
    const calDias = document.querySelectorAll('.cal-d:not(.vazio)');
    
    calDias.forEach(dia => {
        dia.addEventListener('click', function() {
            const diaNum = parseInt(this.textContent);
            const mesAtual = document.querySelector('.cal-mes').textContent;
            
            // Extrair mês e ano do texto
            const [mes, ano] = mesAtual.split(' ');
            const mesNum = getMonthNumber(mes);
            
            // Formatar data para input
            const dataFormatada = `${ano}-${mesNum.toString().padStart(2, '0')}-${diaNum.toString().padStart(2, '0')}`;
            
            // Definir como data de entrega se não estiver definida
            const dataEntregaInput = document.querySelector('input[name="dataEntrega"]');
            if (dataEntregaInput && !dataEntregaInput.value) {
                dataEntregaInput.value = dataFormatada;
                hasUnsavedChanges = true;
            }
        });
    });
}

function getMonthNumber(monthName) {
    const months = {
        'janeiro': '01', 'fevereiro': '02', 'março': '03', 'abril': '04',
        'maio': '05', 'junho': '06', 'julho': '07', 'agosto': '08',
        'setembro': '09', 'outubro': '10', 'novembro': '11', 'dezembro': '12'
    };
    return months[monthName.toLowerCase()] || '01';
}

// ── Notificações ──
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 20px;
        border-radius: 8px;
        color: white;
        font-size: 14px;
        z-index: 1000;
        animation: slideIn 0.3s ease;
        ${type === 'success' ? 'background: #4dff96;' : 'background: #333;'}
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// ── Animações ──
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
    
    .hist-item {
        transition: background-color 0.2s ease;
    }
    .hist-item:hover {
        background: rgba(255,255,255,0.05);
    }
    
    .acao:hover {
        transform: scale(1.1);
        transition: transform 0.1s ease;
    }
`;
document.head.appendChild(style);