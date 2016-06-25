package app.configs;

import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

@Component
public class PrintConfig {

	private DataSource dataSource;
	private JasperDesign design;
	private JasperReport report;
	private JasperPrint print;
	private JasperViewer viewer;

	public JasperDesign getDesign() {
		return design;
	}

	private void setDesign(JasperDesign design) {
		this.design = design;
	}

	public JasperReport getReport() {
		return report;
	}

	private void setReport(JasperReport report) {
		this.report = report;
	}

	public JasperPrint getPrint() {
		return print;
	}

	private void setPrint(JasperPrint print) {
		this.print = print;
	}

	public JasperViewer getViewer() {
		return viewer;
	}

	private void setViewer(JasperViewer viewer) {
		this.viewer = viewer;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 1. lakukan load file .jrxml
	 * 
	 * @param jrxml
	 * @throws JRException
	 */
	public void setValue(String jrxml, HashMap<String, Object> map, JRBeanCollectionDataSource collection)
			throws JRException {
		setPrint(JasperFillManager.fillReport(getClass().getResourceAsStream(jrxml), map, collection));

	}

	public void setValue(String jrxml, HashMap<String, Object> map) throws JRException {
		setPrint(JasperFillManager.fillReport(getClass().getResourceAsStream(jrxml), map, new JREmptyDataSource()));

	}

	public void setValueSQL(String jrxml, HashMap<String, Object> map) throws JRException, SQLException {
		setPrint(JasperFillManager.fillReport(getClass().getResourceAsStream(jrxml), map, dataSource.getConnection()));

	}

	public void setViewer(String title) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				viewer = new JasperViewer(getPrint());
				viewer.setSize(600, 400);
				viewer.setTitle(title);
				viewer.setFitWidthZoomRatio();
				viewer.setVisible(true);
				viewer.requestFocus();
			}
		});

	}

	public void doPrinted() throws JRException {
		JasperPrintManager.printReport(this.getPrint(), false);
	}

}
