package eu.ddmore.workflow.bwf.web.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanBuilder;
import com.sun.faces.mgbean.BeanManager;

public class FacesUtil {

	private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
	private static final int DEFAULT_SENDFILE_BUFFER_SIZE = 10240;
	private static final String ERROR_UNSUPPORTED_ENCODING = "UTF-8 is apparently not supported on this machine.";
	
	public static Object getManagedBean(FacesContext fcCtx, String beanName) {
		ELContext elCtx = fcCtx.getELContext();
		return fcCtx.getELContext().getELResolver().getValue(elCtx, null, beanName);
	} 
	
	@SuppressWarnings("unchecked")
	public static <T> T getManagedBean(FacesContext fcCtx, String name, Class<T> clazz) { 
        return (T)getManagedBean(fcCtx, name); 
    }

    /**
     * Does a regular or ajax redirect.
     */
    public static void doRedirect(FacesContext fcCtx, String redirectPage) throws FacesException {
        ExternalContext ec = fcCtx.getExternalContext();
        try {
            if (ec.isResponseCommitted()) {
                /** Redirect is not possible */
                return;
            }
            /** Fix for renderer kit (Mojarra's and PrimeFaces's ajax redirect) */
            if ((RequestContext.getCurrentInstance().isAjaxRequest() || fcCtx.getPartialViewContext().isPartialRequest()) 
            		&& fcCtx.getResponseWriter() == null && fcCtx.getRenderKit() == null) {
                ServletResponse response = (ServletResponse) ec.getResponse();
                ServletRequest request = (ServletRequest) ec.getRequest();
                response.setCharacterEncoding(request.getCharacterEncoding());
                RenderKitFactory factory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                RenderKit renderKit = factory.getRenderKit(fcCtx, fcCtx.getApplication().getViewHandler().calculateRenderKitId(fcCtx));
                ResponseWriter responseWriter = renderKit.createResponseWriter(response.getWriter(), null, request.getCharacterEncoding());
                fcCtx.setResponseWriter(responseWriter);
            }
            ec.redirect(ec.getRequestContextPath() + (redirectPage != null ? redirectPage : ""));
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }
    
	public static void download(FacesContext fcCtx, String contentType, String filename, boolean attachment, byte[] data) throws Exception {
		OutputStream out = null;
		
		try {
			ExternalContext extCtx = fcCtx.getExternalContext();
			
			// Some JSF component library or some Filter might have set some headers 
			// in the buffer beforehand. We want to get rid of them, else it may collide.
			extCtx.responseReset(); 
		    
			// Prepare the response and set the necessary headers.
			extCtx.setResponseBufferSize(DEFAULT_SENDFILE_BUFFER_SIZE);
			extCtx.setResponseContentType(getMimeType(fcCtx, filename));
			extCtx.setResponseHeader("Content-Disposition", String.format("%s;filename=\"%s\"",
				(attachment ? "attachment" : "inline"), encodeURL(filename)));

			// Not exactly mandatory, but this fixes at least a MSIE quirk: http://support.microsoft.com/kb/316431
			if (((HttpServletRequest) extCtx.getRequest()).isSecure()) {
				extCtx.setResponseHeader("Cache-Control", "public");
				extCtx.setResponseHeader("Pragma", "public");
			}

			if (data != null && data.length > 0) {
				extCtx.setResponseHeader("Content-Length", String.valueOf(data.length));
			}
			
			// Send content
			out = extCtx.getResponseOutputStream();
			out.write(data);
			
			// Important! Otherwise JSF will attempt to render the response which 
			// obviously will fail since it's already written with a file and closed.
			fcCtx.responseComplete(); 
		} finally {
			IOUtils.closeQuietly(out);
		}		
	}
	
	public static String getMimeType(FacesContext fcContext, String name) {
		String mimeType = fcContext.getExternalContext().getMimeType(name);
		if (mimeType == null) {
			mimeType = DEFAULT_MIME_TYPE;
		}
		return mimeType;
	}
	
	public static String encodeURL(String string) {
		if (string == null) {
			return null;
		}
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(ERROR_UNSUPPORTED_ENCODING, e);
		}
	}
	
	public static void printBeans() {
		ApplicationAssociate application = ApplicationAssociate.getInstance(FacesContext.getCurrentInstance().getExternalContext());
		BeanManager beanManager = application.getBeanManager();
		Map<String, BeanBuilder> beanMap = beanManager.getRegisteredBeans();
		Set<Map.Entry<String, BeanBuilder>>beanEntries = beanMap.entrySet();

		for (Map.Entry<String, BeanBuilder> bean: beanEntries) {
			String beanName = bean.getKey();
			if (beanManager.isManaged(beanName)) {
			    BeanBuilder builder = bean.getValue();
			    System.out.println("Bean name: " + beanName);
			    System.out.println("Bean class: " + builder.getBeanClass());
			    System.out.println("Bean scope: " + builder.getScope());
			}
		}
	}
}
