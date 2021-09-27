package br.com.alura.forum.service

import br.com.alura.forum.dto.AtualizacaoTopicoForm
import br.com.alura.forum.dto.NovoTopicoForm
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.Topico
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@Service
class TopicoService(
        private var topicos: List<Topico> = ArrayList(),
        private val topicoViewMapper: TopicoViewMapper,
        private val topicoFormMapper: TopicoFormMapper,
        private val notFoundMessage: String = "Tópico não encontrado"
        ) {

//    init {
//        val topico = Topico(
//                id = 1,
//                titulo = "Dúvida Kotlin",
//                mensagem = "Variáveis no Kotlin",
//                curso = Curso(
//                        id = 1,
//                        nome = "Kotlin",
//                        categoria = "Programação",
//                ),
//                autor = Usuario(
//                        id = 1,
//                        nome = "Ana da Silva",
//                        email = "ana@email.com"
//                )
//        )
//        val topico2 = Topico(
//                id = 2,
//                titulo = "Dúvida Kotlin 2",
//                mensagem = "Variáveis no Kotlin 2",
//                curso = Curso(
//                        id = 1,
//                        nome = "Kotlin",
//                        categoria = "Programação",
//                ),
//                autor = Usuario(
//                        id = 1,
//                        nome = "Ana da Silva",
//                        email = "ana@email.com"
//                )
//        )
//        val topico3 = Topico(
//                id = 3,
//                titulo = "Dúvida Kotlin 3",
//                mensagem = "Variáveis no Kotlin 3",
//                curso = Curso(
//                        id = 1,
//                        nome = "Kotlin",
//                        categoria = "Programação",
//                ),
//                autor = Usuario(
//                        id = 1,
//                        nome = "Ana da Silva",
//                        email = "ana@email.com"
//                )
//        )
//
//        topicos = Arrays.asList(topico, topico2, topico3);
//    }

    fun listar(): List<TopicoView> {

        return topicos.stream().map { t -> topicoViewMapper.map(t) }.collect(Collectors.toList())
    }

    fun buscarPorId(id: Long): TopicoView {
        val topico = topicos.stream().filter { t -> t.id == id }.findFirst().orElseThrow{NotFoundException(notFoundMessage)}
        return topicoViewMapper.map(topico)
    }

    fun cadastrar(form: NovoTopicoForm): TopicoView {
        val topico = topicoFormMapper.map(form)
        topico.id = topicos.size.toLong() + 1
        topicos = topicos.plus(topico)
        return topicoViewMapper.map(topico)
    }

    fun atualizar(form: AtualizacaoTopicoForm): TopicoView {
        val topico = topicos.stream().filter { t -> t.id == form.id }.findFirst().orElseThrow{NotFoundException(notFoundMessage)}
        val topicoAtualizado = Topico(
            id = form.id,
            titulo = form.titulo,
            mensagem = form.mensagem,
            autor = topico.autor,
            dataCriacao = topico.dataCriacao,
            respostas = topico.respostas,
            curso = topico.curso,
            status = topico.status
        )
        topicos = topicos.minus(topico).plus(topicoAtualizado)
        return topicoViewMapper.map(topicoAtualizado)
    }

    fun deletar(id: Long) {
        val topico = topicos.stream().filter { t -> t.id == id }.findFirst().orElseThrow{NotFoundException(notFoundMessage)}
        topicos = topicos.minus(topico)
    }
}