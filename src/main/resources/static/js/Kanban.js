document.addEventListener("DOMContentLoaded", () => {
    // Seleciona todos os cartões e todas as áreas onde eles podem ser soltos
    const cartoes = document.querySelectorAll('.cartao');
    const zonasDeDrop = document.querySelectorAll('.coluna-cartoes');

    let cartaoArrastado = null;

    // --- Configuração dos Cartões ---
    cartoes.forEach(cartao => {
        // Torna a div arrastável pelo navegador
        cartao.setAttribute('draggable', 'true');

        // Quando o usuário COMEÇA a arrastar
        cartao.addEventListener('dragstart', () => {
            cartaoArrastado = cartao;
            // Adiciona um pequeno atraso para a classe de CSS para não bugar o visual do card preso no mouse
            setTimeout(() => cartao.classList.add('arrastando'), 0);
        });

        // Quando o usuário TERMINA de arrastar (soltando o clique)
        cartao.addEventListener('dragend', () => {
            cartao.classList.remove('arrastando');
            cartaoArrastado = null;
        });
    });

    // --- Configuração das Colunas (Zonas de Drop) ---
    zonasDeDrop.forEach(zona => {
        // Quando um cartão está passando por cima da coluna
        zona.addEventListener('dragover', (e) => {
            e.preventDefault(); // Obrigatório para permitir que o item seja "solto" aqui
            zona.classList.add('hover-zona'); // Adiciona um efeito visual na coluna
        });

        // Quando o cartão sai de cima da coluna sem ser solto
        zona.addEventListener('dragleave', () => {
            zona.classList.remove('hover-zona');
        });

        // Quando o usuário SOLTA o cartão na coluna
        zona.addEventListener('drop', (e) => {
            e.preventDefault();
            zona.classList.remove('hover-zona');

            // Move o elemento HTML para esta nova coluna
            if (cartaoArrastado) {
                zona.appendChild(cartaoArrastado);
            }
        });
    });
});