package student;

import operations.*;
import tests.TestHandler;
import tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new pn140041_CityOperations();
        DistrictOperations districtOperations = new pn140041_DistrictOperations(); 
        CourierOperations courierOperations = new pn140041_CourierOperations();
        CourierRequestOperation courierRequestOperation = new pn140041_CourierRequestOperation();
        GeneralOperations generalOperations = new pn140041_GeneralOperations();
        UserOperations userOperations = new pn140041_UserOperations();
        VehicleOperations vehicleOperations = new pn140041_VehicleOperations();
        PackageOperations packageOperations = new pn140041_PackageOperations();

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
