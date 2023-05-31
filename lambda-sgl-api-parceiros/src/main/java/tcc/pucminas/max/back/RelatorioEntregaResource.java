package tcc.pucminas.max.back;

import javax.ws.rs.core.MediaType;

import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
//import org.eclipse.microprofile.rest.client.inject.RestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tcc.pucminas.max.back.model.RelatorioEntrega;

@Path("/entrega")
public class RelatorioEntregaResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Channel("relatorio-entrega-recebido")
    Emitter<RelatorioEntrega> relatorioEntregaEmitter;

    @Channel("relatorio-entrega-recebido-notificacao")
    Emitter<RelatorioEntrega> relatorioEntregaNotificacaoRecebimentoEmitter;

    /**
     * Endpoint que gera um relatório de entrega informando o status atual da entrega {idEntrega}
     * e enviando apra um topica kafka (relatorio-entrega-recebido).
     */
    @POST
    @Path("/{idEntrega}/relatorio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarNovoRegistrosRelatorioEntrega(@PathParam("idEntrega") long idEntrega,
        RelatorioEntrega relatorioEntrega) {

        logger.info("********************************** POST reg rel entrega idEntrega = [{}] e registro = [{}] **********************************",
            idEntrega, relatorioEntrega);

        UUID uuidRelatorio = UUID.randomUUID();

        if(relatorioEntrega.id_entrega != idEntrega) {
            logger.info("id_entrega diferente do id passado no endpoint.");
            return Response.status(400).build();
        }

        relatorioEntrega.uuid = uuidRelatorio.toString();

        //if(relatorioEntrega.data == null) {
            logger.info("O campo data foi ajustado para data e hora atual");
            //Long datetime = System.currentTimeMillis();

            relatorioEntrega.AtualizarCampoData();
        //}
        logger.info("valor da data = [{}]", relatorioEntrega.data);

        logger.info("Enviando solicitação via mensageria para o serviço sge-entregas");
        logger.info("[{}]", relatorioEntrega);

        relatorioEntregaEmitter.send(relatorioEntrega);

        
        logger.info("Enviando solicitação via mensageria para o serviço extrator de dados (notificação)");
        logger.info("[{}]", relatorioEntrega);

        relatorioEntregaNotificacaoRecebimentoEmitter.send(relatorioEntrega);

        return Response.status(200).entity(relatorioEntrega).build();
    }
    
}
