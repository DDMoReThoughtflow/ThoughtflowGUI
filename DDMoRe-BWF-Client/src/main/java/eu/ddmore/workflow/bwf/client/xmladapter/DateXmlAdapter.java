package eu.ddmore.workflow.bwf.client.xmladapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateXmlAdapter extends XmlAdapter<String, Date> {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Override
    public String marshal(Date v) throws Exception {
        synchronized (this.dateFormat) {
            return this.dateFormat.format(v);
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        synchronized (this.dateFormat) {
            return this.dateFormat.parse(v);
        }
    }
}
