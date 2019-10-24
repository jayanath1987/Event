package lk.icta.events.eventAttendance.view.readQR;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/invitationAcceptance")
public class QRReadWebService {
	@RequestMapping(value="/{name}", method = RequestMethod.GET)
	public @ResponseBody String getShopInJSON(@PathVariable String name) {

		return "Test";

	}
	
	
}
