package student;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import operations.PackageOperations;
import operations.PackageOperations.Pair;
import student.helper.SpExecutor;

public class pn140041_PackageOperations implements PackageOperations {

	@Override
	public boolean acceptAnOffer(int arg0) {
		return SpExecutor.ExecuteAcceptOffer(arg0);
	}

	@Override
	public boolean changeType(int arg0, int arg1) {
		return SpExecutor.ExecuteChangePackageType(arg0,  arg1);
	}

	@Override
	public boolean changeWeight(int arg0, BigDecimal arg1) {
		if(arg1 == null)
			return false;
		return SpExecutor.ExecuteChangePackageWeight(arg0, arg1);
	}

	@Override
	public boolean deletePackage(int arg0) {
		return SpExecutor.ExecuteDeletePackage(arg0);
	}

	@Override
	public int driveNextPackage(String arg0) {
		if(arg0 == null || arg0.isEmpty())
			return -2;
		return SpExecutor.ExecuteDriveNextPackage(arg0);
	}

	@Override
	public Date getAcceptanceTime(int arg0) {
		return SpExecutor.ExecuteGetPackageAcceptanceDate(arg0);
	}

	@Override
	public List<Integer> getAllOffers() {
		return SpExecutor.ExecuteGetAllOffers();
	}

	@Override
	public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int arg0) {
		return SpExecutor.ExecuteGetAllOffersForPackage(arg0);
	}

	@Override
	public List<Integer> getAllPackages() {
		return SpExecutor.ExecuteGetAllPackages();
	}

	@Override
	public List<Integer> getAllPackagesWithSpecificType(int arg0) {
		return SpExecutor.ExecuteGetAllPackagesWithType(arg0);
	}

	@Override
	public Integer getDeliveryStatus(int arg0) {
		return SpExecutor.ExecuteGetPackageDeliveryStatus(arg0);
	}

	@Override
	public List<Integer> getDrive(String arg0) {
		return SpExecutor.ExecuteGetDrive(arg0);
	}

	@Override
	public BigDecimal getPriceOfDelivery(int arg0) {
		return SpExecutor.ExecuteGetPriceOfPackageDelivery(arg0);
	}

	@Override
	public int insertPackage(int arg0, int arg1, String arg2, int arg3, BigDecimal arg4) {
		return SpExecutor.ExecuteInsertPackage(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public int insertTransportOffer(String arg0, int arg1, BigDecimal arg2) {
		return SpExecutor.ExecuteInsertTransportOffer(arg0, arg1, arg2);
	}
	
	public static class PackagePair<A,B> implements Pair<A, B> {

		private A firstParam;
		private B secondParam;
		
		@Override
		public A getFirstParam() {
			return firstParam;
		}

		@Override
		public B getSecondParam() {
			return secondParam;
		}
		
		public PackagePair(A integer, B bigDecimal){
			firstParam = integer;
			secondParam = bigDecimal;
		}

	}
}
