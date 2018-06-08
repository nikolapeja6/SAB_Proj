package student;

import operations.*;
import tests.TestHandler;
import tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new pn140041_CityOperations();
        DistrictOperations districtOperations = null; // Do it for all classes.
        CourierOperations courierOperations = null; // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = null;
        GeneralOperations generalOperations = new pn140041_GeneralOperations();
        UserOperations userOperations = null;
        VehicleOperations vehicleOperations = null;
        PackageOperations packageOperations = null;

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
}
