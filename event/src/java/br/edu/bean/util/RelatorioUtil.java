/*
 * C�digo-fonte do livro "Programa��o Java para a Web"
 * Autores: D�cio Heinzelmann Luckow <decioluckow@gmail.com>
 *          Alexandre Altair de Melo <alexandremelo.br@gmail.com>
 *
 * ISBN: 978-85-7522-238-6
 * http://www.javaparaweb.com.br
 * http://www.novatec.com.br/livros/javaparaweb
 * Editora Novatec, 2010 - todos os direitos reservados
 *
 * LICEN�A: Este arquivo-fonte est� sujeito a Atribui��o 2.5 Brasil, da licen�a Creative Commons,
 * que encontra-se dispon�vel no seguinte endere�o URI: http://creativecommons.org/licenses/by/2.5/br/
 * Se voc� n�o recebeu uma c�pia desta licen�a, e n�o conseguiu obt�-la pela internet, por favor,
 * envie uma notifica��o aos seus autores para que eles possam envi�-la para voc� imediatamente.
 *
 *
 * Source-code of "Programa��o Java para a Web" book
 * Authors: D�cio Heinzelmann Luckow <decioluckow@gmail.com>
 *          Alexandre Altair de Melo <alexandremelo.br@gmail.com>
 *
 * ISBN: 978-85-7522-238-6
 * http://www.javaparaweb.com.br
 * http://www.novatec.com.br/livros/javaparaweb
 * Editora Novatec, 2010 - all rights reserved
 *
 * LICENSE: This source file is subject to Attribution version 2.5 Brazil of the Creative Commons
 * license that is available through the following URI:  http://creativecommons.org/licenses/by/2.5/br/
 * If you did not receive a copy of this license and are unable to obtain it through the web, please
 * send a note to the authors so they can mail you a copy immediately.
 *
 */
package br.edu.bean.util;

import br.edu.util.ConnectionFactory;
import br.edu.util.HibernateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import br.edu.util.UtilException;

public class RelatorioUtil {

	public static final int	RELATORIO_PDF							= 1;
	public static final int	RELATORIO_EXCEL						= 2;
	public static final int	RELATORIO_HTML							= 3;
	public static final int	RELATORIO_PLANILHA_OPEN_OFFICE	= 4;

	public StreamedContent geraRelatorio(HashMap parametrosRelatorio, String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio) throws UtilException {
		StreamedContent arquivoRetorno = null;

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Connection conexao = this.getConexao();
			String caminhoRelatorio = context.getExternalContext().getRealPath("relatorios");
			String caminhoArquivoJasper = caminhoRelatorio + File.separator + nomeRelatorioJasper + ".jasper";
			String caminhoArquivoRelatorio = null;

			JasperReport relatorioJasper = (JasperReport) JRLoader.loadObject(caminhoArquivoJasper);
			JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, conexao);
			JRExporter tipoArquivoExportado = null;
			String extensaoArquivoExportado = "";
			File arquivoGerado = null;

			switch (tipoRelatorio) {
				case RelatorioUtil.RELATORIO_PDF :
					tipoArquivoExportado = new JRPdfExporter();
					extensaoArquivoExportado = "pdf";
					break;
				case RelatorioUtil.RELATORIO_HTML :
					tipoArquivoExportado = new JRHtmlExporter();
					extensaoArquivoExportado = "html";
					break;
				case RelatorioUtil.RELATORIO_EXCEL :
					tipoArquivoExportado = new JRXlsExporter();
					extensaoArquivoExportado = "xls";
					break;
				case RelatorioUtil.RELATORIO_PLANILHA_OPEN_OFFICE :
					tipoArquivoExportado = new JROdtExporter();
					extensaoArquivoExportado = "ods";
					break;
				default :
					tipoArquivoExportado = new JRPdfExporter();
					extensaoArquivoExportado = "pdf";
					break;
			}
			caminhoArquivoRelatorio = caminhoRelatorio + File.separator + nomeRelatorioSaida + "." + extensaoArquivoExportado;
			arquivoGerado = new java.io.File(caminhoArquivoRelatorio);
			tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
			tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
			tipoArquivoExportado.exportReport();
			arquivoGerado.deleteOnExit();

			InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
			arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensaoArquivoExportado, nomeRelatorioSaida + "." + extensaoArquivoExportado);
		} catch (JRException e) {
			throw new UtilException("Não foi possível gerar o relatório.", e);
		} catch (FileNotFoundException e) {
			throw new UtilException("Arquivo do relatório não encontrado.", e);
		}
		return arquivoRetorno;
	}

	private Connection getConexao() throws UtilException {
		java.sql.Connection conexao = null;
		try {
			conexao = new ConnectionFactory().getConnection();
		} catch (Exception e) {
			throw new UtilException("N�o foi poss�vel encontrar o nome da conex�o do banco.", e);
		}
		return conexao;
	}
}
