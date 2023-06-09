package tcc.pucminas.max.back;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.reactive.messaging.annotations.Blocking;

import tcc.pucminas.max.back.model.RelatorioEntrega;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelatorioEntregaProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());


    /*
    * Recebe da stream relatorio-entrega criada a partir do tópico relatorio-entrega-recebido
    * e salvar no banco de Dados (aqui apenas memória)
    */
    @Incoming("relatorio-entrega-in")
    @Blocking
    public void process(RelatorioEntrega relatorioEntrega) throws InterruptedException {
        logger.info("@@@@@@@@@@@@@@@@@@@ Tratando canal relatorio-entrega @@@@@@@@@@@@@@@@@@@@@@@");
        //Thread.sleep(2000);
        //logger.info("teria salvo no banco agora !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        logger.info("Objeto recebido: [{}]", relatorioEntrega);
    }
    
}
