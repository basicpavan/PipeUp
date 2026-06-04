// ── Funcionalidades do Dashboard ──

document.addEventListener('DOMContentLoaded', function() {
    
    // ── Drag and Drop para as tarefas ──
    initDragAndDrop();
    
    // ── Pesquisa em tempo real ──
    initSearch();
    
    // ── Atalhos de teclado ──
    initKeyboardShortcuts();
    
    // ── Tooltips ──
    initTooltips();
});

// ── Drag and Drop ──
function initDragAndDrop() {
    const cartoes = document.querySelectorAll('.cartao');
    const colunas = document.querySelectorAll('.coluna-cartoes');
    
    cartoes.forEach(cartao => {
        cartao.draggable = true;
        
        cartao.addEventListener('dragstart', function(e) {
            const tarefaId = this.href.split('/').pop();
            const titulo = this.dataset.titulo;
            const espacoId = this.dataset.espacoId;

            e.dataTransfer.setData('text/plain', tarefaId); // Only transfer ID for simplicity
            e.dataTransfer.setData('text/titulo', titulo);
            e.dataTransfer.setData('text/espacoId', espacoId);
            
            this.style.opacity = '0.5';
        });
        
        cartao.addEventListener('dragend', function() {
            this.style.opacity = '1';
        });
    });
    
    colunas.forEach(coluna => {
        coluna.addEventListener('dragover', function(e) {
            e.preventDefault();
            this.style.background = 'rgba(251, 125, 41, 0.1)';
        });
        
        coluna.addEventListener('dragleave', function() {
            this.style.background = '';
        });
        
        coluna.addEventListener('drop', function(e) {
            e.preventDefault();
            this.style.background = '';
            
            const tarefaId = e.dataTransfer.getData('text/plain');
            const titulo = e.dataTransfer.getData('text/titulo');
            const espacoId = e.dataTransfer.getData('text/espacoId');
            const novoStatus = getStatusFromColumn(this);
            
            if (novoStatus && tarefaId && titulo && espacoId) {
                updateTaskStatus(tarefaId, titulo, espacoId, novoStatus);
            }
        });
    });
}

function getStatusFromColumn(coluna) {
    const tituloColuna = coluna.parentElement.querySelector('.coluna-topo span').textContent;
    const statusMap = {
        'Em Atraso': 'EM_ATRASO',
        'Em Andamento': 'EM_ANDAMENTO', 
        'A Iniciar': 'A_INICIAR',
        'Concluído': 'CONCLUIDO'
    };
    return statusMap[tituloColuna];
}

function updateTaskStatus(tarefaId, titulo, espacoId, novoStatus) {
    fetch(`/tarefas/${tarefaId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `titulo=${encodeURIComponent(titulo)}&espacoId=${espacoId}&status=${novoStatus}`
    })
    .then(response => {
        if (response.ok) {
            location.reload();
        } else {
            // Handle errors, e.g., show a message to the user
            response.text().then(text => console.error('Erro ao atualizar tarefa:', text));
            alert('Erro ao atualizar o status da tarefa.');
        }
    })
    .catch(error => console.error('Erro na requisição:', error));
}

// ── Pesquisa em tempo real ──
function initSearch() {
    const searchInput = document.querySelector('input[name="filtro"]');
    if (!searchInput) return;
    
    let timeout;
    searchInput.addEventListener('input', function() {
        clearTimeout(timeout);
        timeout = setTimeout(() => {
            const filtro = this.value.trim();
            filterTasks(filtro);
        }, 300);
    });
}

function filterTasks(filtro) {
    const cartoes = document.querySelectorAll('.coluna-cartoes .cartao'); // Select only cards within columns
    
    cartoes.forEach(cartao => {
        const titulo = cartao.querySelector('.cartao-titulo').textContent.toLowerCase();
        const match = titulo.includes(filtro.toLowerCase());
        
        cartao.style.display = match ? 'block' : 'none';
    });
}

// ── Atalhos de teclado ──
function initKeyboardShortcuts() {
    document.addEventListener('keydown', function(e) {
        // Ctrl + N = Nova tarefa
        if (e.ctrlKey && e.key === 'n') {
            e.preventDefault();
            window.location.href = '/tarefas/nova';
        }
        
        // Ctrl + / = Focar na pesquisa
        if (e.ctrlKey && e.key === '/') {
            e.preventDefault();
            const searchInput = document.querySelector('input[name="filtro"]');
            if (searchInput) {
                searchInput.focus();
                searchInput.select();
            }
        }
        
        // ESC = Limpar pesquisa
        if (e.key === 'Escape') {
            const searchInput = document.querySelector('input[name="filtro"]');
            if (searchInput && searchInput.value) {
                searchInput.value = '';
                filterTasks('');
            }
        }
    });
}

// ── Tooltips ──
function initTooltips() {
    const buttons = document.querySelectorAll('.header-btn, .coluna-btn, .btn-mais-laranja');
    
    buttons.forEach(btn => {
        btn.addEventListener('mouseenter', function(e) {
            showTooltip(e.target, getTooltipText(e.target));
        });
        
        btn.addEventListener('mouseleave', function() {
            hideTooltip();
        });
    });
}

function getTooltipText(element) {
    if (element.querySelector('img')) {
        const alt = element.querySelector('img').alt;
        return alt || 'Ação';
    }
    if (element.textContent === '+') {
        return 'Nova tarefa (Ctrl+N)';
    }
    return 'Ação';
}

function showTooltip(element, text) {
    const tooltip = document.createElement('div');
    tooltip.className = 'tooltip';
    tooltip.textContent = text;
    tooltip.style.cssText = `
        position: absolute;
        background: #333;
        color: white;
        padding: 4px 8px;
        border-radius: 4px;
        font-size: 12px;
        z-index: 1000;
        pointer-events: none;
        white-space: nowrap;
    `;
    
    document.body.appendChild(tooltip);
    
    const rect = element.getBoundingClientRect();
    tooltip.style.left = rect.left + (rect.width / 2) - (tooltip.offsetWidth / 2) + 'px';
    tooltip.style.top = rect.bottom + 5 + 'px';
}

function hideTooltip() {
    const tooltip = document.querySelector('.tooltip');
    if (tooltip) {
        tooltip.remove();
    }
}

// ── Animações suaves ──
function addSmoothAnimations() {
    const style = document.createElement('style');
    style.textContent = `
        .cartao {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .cartao:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        }
        .coluna-cartoes {
            transition: background-color 0.2s ease;
        }
    `;
    document.head.appendChild(style);
}

// Inicializar animações
addSmoothAnimations();