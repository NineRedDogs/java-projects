package ajg.adid.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleDataGenerator {

	private static final int MAX_NUM_ROWS = 50;

	private static final int ADD_MORE_VEHICLES_TO_POLICY = 50;
	private static final int ADD_FAMILY_MEMBER_TO_MOTOR_POLICY = 20;
	private static final int ADD_TWIN_TO_MOTOR_POLICY = 10000;
	private static final int ADD_MORE_POLICIES_FOR_CUSTOMER = 30;
	private static final int MAX_NUM_VEHICLES_PER_POLICY = 10;


	private final AddressGenerator addrGen = new AddressGenerator();
	private final DeviceIdGenerator devIdGen = new DeviceIdGenerator();
	private final DlnGenerator dlnGen = new DlnGenerator();
	private final DobGenerator dobGen = new DobGenerator();
	private final EmailGenerator emailGen = new EmailGenerator();
	private final MobileGenerator mobileGen = new MobileGenerator();
	private final NameGenerator nameGen = new NameGenerator();
	private final PolicyGenerator policyGen = new PolicyGenerator();
	private final RegNumGenerator regNumGen = new RegNumGenerator();

	private final Random rnd = new Random();
	private final Chance chance = new Chance();


	public List<CsvRow> generateRows() {

		int rowId=0;
		List<CsvRow> rows = new ArrayList<CsvRow>();

		while (rowId < MAX_NUM_ROWS) {

			final String forename = nameGen.getForename();
			final String surname = nameGen.getSurname();
			final AdmiralPolicy pol = policyGen.generatePolicy();
			final Address addr = addrGen.getAddress();
			final String dob = dobGen.generateDob();
			final String mobile = mobileGen.generateMobile();
			final String email = emailGen.generateEmailAddress(forename, surname);
			final VehicleInfo vehicle = regNumGen.generateVehicleData();
			final String dln = dlnGen.generateDLN(surname);
			final String deviceId = devIdGen.generateDeviceId();

			// create row
			CsvRow row = new CsvRow(Integer.toString(rowId),
					pol.getProduct(),
					pol.getPolicyNumber(),
					forename,
					surname,
					addr.getAddress1(),
					addr.getAddress2(),
					addr.getAddress3(),
					addr.getPostcode(),
					dob,
					mobile,
					email,
					pol.isMotor() ? vehicle.getRegNum() : "",
							pol.isMotor() ? dln : "",
									deviceId,
									pol.isMotor() ? vehicle.getAbiCode() : "",
											addr.getAlfKey(),
											pol.getDateInception(),
											pol.getDateExpired(),
											pol.getDateOriginated(),
											pol.getDateCancelled());
			rows.add(row);
			rowId++;

			// now for some optional extras .....
			// add family member to a motor policy ???
			if (pol.isMotor()) {
				
				if (chance.feelingLuckyPunk(ADD_MORE_VEHICLES_TO_POLICY)) {
					for (int i = 0; i < rnd.nextInt(MAX_NUM_VEHICLES_PER_POLICY); i++) {
						final VehicleInfo newVehicle = regNumGen.generateVehicleData();

						// create row
						CsvRow newRow = new CsvRow(Integer.toString(rowId),
								pol.getProduct(),
								pol.getPolicyNumber(),
								forename,
								surname,
								addr.getAddress1(),
								addr.getAddress2(),
								addr.getAddress3(),
								addr.getPostcode(),
								dob,
								mobile,
								email,
								newVehicle.getRegNum(),
								dln,
								deviceId,
								newVehicle.getAbiCode(),
								addr.getAlfKey(),
								pol.getDateInception(),
								pol.getDateExpired(),
								pol.getDateOriginated(),
								pol.getDateCancelled());
						rows.add(newRow);
						rowId++;
					}
				}	
		
				if (chance.feelingLuckyPunk(ADD_FAMILY_MEMBER_TO_MOTOR_POLICY)) {
					final String familyForename = nameGen.getForename();
					final String familyDob = dobGen.generateDob();
					final String familyEmail = emailGen.generateEmailAddress(familyForename, surname);
					final String familyMobile = mobileGen.generateMobile();
					final String familyDln = dlnGen.generateDLN(surname);

					CsvRow familyRow = new CsvRow(Integer.toString(rowId),
							pol.getProduct(),
							pol.getPolicyNumber(),
							familyForename,
							surname,
							addr.getAddress1(),
							addr.getAddress2(),
							addr.getAddress3(),
							addr.getPostcode(),
							familyDob,
							familyMobile,
							familyEmail,
							vehicle.getRegNum(),
							familyDln,
							deviceId,
							vehicle.getAbiCode(),
							addr.getAlfKey(),
							pol.getDateInception(),
							pol.getDateExpired(),
							pol.getDateOriginated(),
							pol.getDateCancelled());
					rows.add(familyRow);
					rowId++;
				}


				// add twin to a motor policy ???
				if (chance.feelingLuckyPunk(ADD_TWIN_TO_MOTOR_POLICY)) {
					final String twinForename = nameGen.getForename();
					final String twinEmail = emailGen.generateEmailAddress(twinForename, surname);
					final String twinMobile = mobileGen.generateMobile();
					final String twinDln = dlnGen.generateDLN(surname);

					CsvRow twinRow = new CsvRow(Integer.toString(rowId),
							pol.getProduct(),
							pol.getPolicyNumber(),
							twinForename,
							surname,
							addr.getAddress1(),
							addr.getAddress2(),
							addr.getAddress3(),
							addr.getPostcode(),
							dob,
							twinMobile,
							twinEmail,
							vehicle.getRegNum(),
							twinDln,
							deviceId,
							vehicle.getAbiCode(),
							addr.getAlfKey(),
							pol.getDateInception(),
							pol.getDateExpired(),
							pol.getDateOriginated(),
							pol.getDateCancelled());
					rows.add(twinRow);
					rowId++;
				}

				// add other policies for this customer ??
				if (chance.feelingLuckyPunk(ADD_MORE_POLICIES_FOR_CUSTOMER)) {
					for (int i = 0; i < rnd.nextInt(policyGen.getNumProducts()-1); i++) {
						final AdmiralPolicy newPol = policyGen.generatePolicy();
						final VehicleInfo newVehicle = regNumGen.generateVehicleData();

						// create row
						CsvRow newRow = new CsvRow(Integer.toString(rowId),
								newPol.getProduct(),
								newPol.getPolicyNumber(),
								forename,
								surname,
								addr.getAddress1(),
								addr.getAddress2(),
								addr.getAddress3(),
								addr.getPostcode(),
								dob,
								mobile,
								email,
								newPol.isMotor() ? newVehicle.getRegNum() : "",
										newPol.isMotor() ? dln : "",
												deviceId,
												newPol.isMotor() ? newVehicle.getAbiCode() : "",
														addr.getAlfKey(),
														newPol.getDateInception(),
														newPol.getDateExpired(),
														newPol.getDateOriginated(),
														newPol.getDateCancelled());
						rows.add(newRow);
						rowId++;
					}
				}			
			}
		}
		return rows;
	}
	
	public static void main(String[] args) throws IOException {
		final SampleDataGenerator sdg = new SampleDataGenerator();
		
		final String CSV_FILE = System.getProperty("user.home") + File.separator + "generated.csv";
		
		FileWriter writer = new FileWriter(CSV_FILE);		  
		List<CsvRow> rows = sdg.generateRows();
		
		writer.write(CsvRow.headers() + "\n");

		for (CsvRow csvRow : rows) {
			writer.write(csvRow.toString() + "\n");
		}
		writer.close();
	}
}
