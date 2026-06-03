let etapaAtual = "";

function abrirModal(etapa) {

    etapaAtual = etapa;

    document.getElementById("modalTarefa")
        .style.display = "flex";
}

function fecharModal() {

    document.getElementById("modalTarefa")
        .style.display = "none";
}

async function cadastrarTarefa() {

    const tarefa = {

        nome: document.getElementById("nome").value,

        descricao: document.getElementById("descricao").value,

        etapa: etapaAtual
    };

    const response = await fetch("/tarefas", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(tarefa)
    });

    const tarefaCriada = await response.json();

    adicionarCard(tarefaCriada);

    fecharModal();
}

function adicionarCard(tarefa) {

    const coluna = document.getElementById(tarefa.etapa);

    const card = document.createElement("div");

    card.classList.add("card-tarefa");

    card.innerHTML = `
        <h4>${tarefa.nome}</h4>
        <p>${tarefa.descricao}</p>
    `;

    coluna.appendChild(card);
}