package DCS_FuzzyLab;

import java.util.ArrayList;
import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataFuzzy;
import DataOnly.FLRS;
import DataOnly.FV;
import DataOnly.Fuzzy;
import DataOnly.PlaceNameWithWeight;
import Enumerations.FZ;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class AdderSubtractor {
	public static void main (String[]args) {
	PetriNet pn = new PetriNet();
	pn.PetriNetName = "AdderSubtractor";
	pn.NetworkPort = 1082;

	// test values - change these for the tests
	float x1 = 0.2F;
	float x2 = 0.3F;
	float w1 = 1F;
	float w2 = 2F;

	// Table 3: rows = x1 (NL, NM, ZR, PM, PL), columns = x2 (NL, NM, ZR, PM, PL)
	// each FV = (x3, x4) where x3 = w1*x1 + w2*x2 and x4 = w1*x1 - w2*x2
	FLRS flrsAddSub = new FLRS(new FV(FZ.NL, FZ.ZR), new FV(FZ.NL, FZ.NM), new FV(FZ.NL, FZ.NL), new FV(FZ.NM, FZ.NL), new FV(FZ.ZR, FZ.NL),
							   new FV(FZ.NL, FZ.PM), new FV(FZ.NL, FZ.ZR), new FV(FZ.NM, FZ.NM), new FV(FZ.ZR, FZ.NL), new FV(FZ.PM, FZ.NL),
							   new FV(FZ.NL, FZ.PL), new FV(FZ.NM, FZ.PM), new FV(FZ.ZR, FZ.ZR), new FV(FZ.PM, FZ.NM), new FV(FZ.PL, FZ.NL),
							   new FV(FZ.NM, FZ.PL), new FV(FZ.ZR, FZ.PL), new FV(FZ.PM, FZ.PM), new FV(FZ.PL, FZ.ZR), new FV(FZ.PL, FZ.NM),
							   new FV(FZ.ZR, FZ.PL), new FV(FZ.PM, FZ.PL), new FV(FZ.PL, FZ.PL), new FV(FZ.PL, FZ.PM), new FV(FZ.PL, FZ.ZR));

	flrsAddSub.Print();

	DataFuzzy p1 = new DataFuzzy();
	p1.SetName("P1");
	p1.SetValue(new Fuzzy(x1));
	pn.PlaceList.add(p1);

	DataFuzzy p2 = new DataFuzzy();
	p2.SetName("P2");
	p2.SetValue(new Fuzzy(x2));
	pn.PlaceList.add(p2);

	DataFuzzy p3 = new DataFuzzy();
	p3.SetName("P3");
	pn.PlaceList.add(p3);

	DataFuzzy p4 = new DataFuzzy();
	p4.SetName("P4");
	pn.PlaceList.add(p4);

	// T1 ------------------------------------------------
			PetriTransition t1 = new PetriTransition(pn);
			t1.TransitionName = "T1";
			t1.InputPlaceName.add("P1");
			t1.InputPlaceName.add("P2");

			Condition T1Ct1 = new Condition(t1, "P1", TransitionCondition.NotNull);
			Condition T1Ct2 = new Condition(t1, "P2", TransitionCondition.NotNull);
			T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

			GuardMapping grdT1 = new GuardMapping();
			grdT1.condition = T1Ct1;

			ArrayList<PlaceNameWithWeight> input = new ArrayList<>();
			input.add(new PlaceNameWithWeight("P1", w1));
			input.add(new PlaceNameWithWeight("P2", w2));

			ArrayList<String> twoOutput = new ArrayList<>();
			twoOutput.add("P3");   // x3 = w1*x1 + w2*x2
			twoOutput.add("P4");   // x4 = w1*x1 - w2*x2

			grdT1.Activations.add(new Activation(t1, flrsAddSub, input, TransitionOperation.FLRS, twoOutput));

			t1.GuardMappingList.add(grdT1);

			t1.Delay = 0;
			pn.Transitions.add(t1);

			// -------------------------------------------

			System.out.println("AdderSubtractor started \n ------------------------------");
			pn.Delay = 3000;
			pn.Start();

			PetriNetWindow frame = new PetriNetWindow(false);
			frame.petriNet = pn;
			frame.setVisible(true);
	}
}
