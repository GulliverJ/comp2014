package test;

// NOTE: this was used at an earlier stage for testing all methods at once. The methods haven't changed - the new version still works, but some have moved around now. I'm leaving this code here for documentation purposes later!

public class APITester {
	/*
	String cred = "apitest";
	private static OrangeConnection api = null;
	
	@BeforeClass
	public static void setUp() {
		api = new OrangeConnection("apitest", "apitest");
	}

	@AfterClass
	public static void tearDown() {
		api.close();
	}
	
	@Test
	public void testGetIDsWhereSingle() {
		int[] ids = api.getIDsWhere("application", "Test Parking");
		assertEquals(3, ids.length);
		assertEquals(21, ids[0]);
		assertEquals(22, ids[1]);
		assertEquals(23, ids[2]);
	}
	
	@Test
	public void testGetIDsWhereMult() {
		String[] cols = {"application", "unit"};
		String[] args = {"Test Parking", "cm"};
		int[] ids = api.getIDsWhere(cols, args);
		assertEquals(2, ids.length);
		assertEquals(21, ids[0]);
		assertEquals(22, ids[1]);
	}
	
	@Test
	public void testGetMyIDs() {
		int[] ids = api.getMyIDs();
		assertEquals(6, ids.length);
		assertEquals(21, ids[0]);
		assertEquals(26, ids[5]);
	}
	
	
	@Test
	public void testGetMyIDsList() {
		int[] request = {4, 5, 1};
		int[] ids = api.getMyIDs(request);
		assertEquals(3, ids.length);
		assertEquals(21, ids[0]);
		assertEquals(24, ids[1]);
		assertEquals(25, ids[2]);
	}
	
	
	@Test
	public void testGetMyIDsWhereSingle() {
		int[] ids = api.getMyIDsWhere("application", "Test Parking");
		assertEquals(3, ids.length);
		assertEquals(21, ids[0]);
		assertEquals(23, ids[2]);
		
		// Null test
		ids = api.getMyIDsWhere("application", "Not an application");
		assertEquals(0, ids.length);
	}
	
	@Test
	public void testGetMyIDsWhereMult() {
		String[] cols = {"application", "unit"};
		String[] args = {"Test Parking", "cm"};
		int[] ids = api.getMyIDsWhere(cols, args);
		assertEquals(2, ids.length);
		assertEquals(21, ids[0]);
		assertEquals(22, ids[1]);
		String[] falseArgs = {"Orange Parking", "cm"};
		ids = api.getMyIDsWhere(cols, falseArgs);
		assertEquals(0, ids.length);
	}
	
	@Test
	public void testGetLastReading() {
		float res = api.getLastReading(21);
		assertEquals(29, (int) res);
		res = api.getLastReading(24);
		assertEquals(17, (int) res);
	}

	@Test
	public void testGetLastReadingList() {
		int[] ids = api.getMyIDsWhere("application", "Test Parking");
		float[] results = api.getLastReadingList(ids);
		assertEquals(29, (int) results[0]);
		assertEquals(21, (int) results[1]);
		assertEquals(33, (int) results[2]);	
	}
	
	@Test
	public void testGetLastTimestamp() {
		DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date res = api.getLastTimestamp(21);
		assertTrue(ft.format(res).equals("2015-04-08 21:27:57"));
	}
	
	@Test
	public void testGetLastTimestampList() {
		int[] ids = {21, 22};
		DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date[] res = api.getLastTimestampList(ids);
		assertTrue(ft.format(res[0]).equals("2015-04-08 21:27:57"));
		assertTrue(ft.format(res[1]).equals("2015-04-08 21:28:08"));
	}

	
	@Test
	public void testGetApplication() {
		assertEquals("Test Parking", api.getApplication(21));
		assertFalse(api.getApplication(22).equals("Not an application"));
	}
	
	@Test
	public void testGetApplicationList() {
		int[] ids = {21, 24};
		String[] apps = api.getApplicationList(ids);
		assertEquals("Test Parking", apps[0]);
		assertEquals("Test Temp", apps[1]);
	}
	
	@Test
	public void testGetMeasures() {
		assertTrue(api.getMeasures(21).equals("DISTANCE"));
		assertTrue(api.getMeasures(25).equals("TEMPERATURE"));
	}
	
	@Test
	public void testGetMeasuresList() {
		int[] ids = {21, 25};
		String[] res = api.getMeasuresList(ids);
		assertEquals(res[0], "DISTANCE");
		assertEquals(res[1], "TEMPERATURE");
	}
	
	@Test
	public void testGetUnit() {
		assertTrue(api.getUnit(21).equals("cm"));
		assertTrue(api.getUnit(23).equals("m"));
	}
	
	@Test
	public void testGetUnitList() {
		int[] ids = {21, 23};
		String[] res = api.getUnitList(ids);
		assertEquals(res[0], "cm");
		assertEquals(res[1], "m");
	}
	
	
	@Test
	public void testGetLat() {
		Double expected = 51.533388;
		assertTrue(expected.equals(api.getLat(26)));
	}
	
	@Test
	public void testGetLatList() {
		int[] ids = {25, 26};
		Double[] expected = {51.536, 51.533388};
		double[] res = api.getLatList(ids);
		assertTrue(expected[0].equals(res[0]));
		assertTrue(expected[1].equals(res[1]));
	}
	
	@Test
	public void testGetLng() {
		Double expected = -0.100321;
		assertTrue(expected.equals(api.getLng(26)));
	}
	
	@Test
	public void testGetLngList() {
		int[] ids = {25, 26};
		Double[] expected = {-0.09681, -0.100321};
		double[] res = api.getLngList(ids);
		assertTrue(expected[0].equals(res[0]));
		assertTrue(expected[1].equals(res[1]));
	}

	@Test
	public void testGetDateAdded() {
		DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date res = api.getDateAdded(21);
		assertTrue(ft.format(res).equals("2015-04-08 21:05:19"));
	}
	
	@Test
	public void testGetDateAddedList() {
		int[] ids = {21, 22};
		DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date[] res = api.getDateAddedList(ids);
		assertTrue(ft.format(res[0]).equals("2015-04-08 21:05:19"));
		assertTrue(ft.format(res[1]).equals("2015-04-08 21:05:43"));
	}
	
	
	@Test
	public void testGetPosition() {
		Position exp = new Position(21, 51.533805, -0.099562);
		Position res = api.getPosition(21);
		assertEquals(exp.getID(), res.getID());
		if(exp.getLat() != res.getLat()) {
			fail();
		}
		if(exp.getLng() != res.getLng()) {
			fail();
		}
	}
	
	@Test
	public void testGetPositionList() {
		int[] ids = {21, 22};
		Position exp1 = new Position(21, 51.533805, -0.099562);
		Position exp2 = new Position(22, 51.533828, -0.101022);
		Position[] res = api.getPositionList(ids);
		assertEquals(res[0].getID(), 21);
		assertEquals(res[1].getID(), 22);
		if(exp1.getLat() != res[0].getLat() || exp1.getLng() != res[0].getLng()) {
			fail();
		}
		if(exp2.getLat() != res[1].getLat() || exp2.getLng() != res[1].getLng()) {
			fail();
		}
	}
	
	@Test
	public void testGetDataSince() {
		DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = null;
		try {
			dt = ft.parse("2015-04-08 21:20:00");
		} catch(Exception e) {
			fail();
		}
		SensorData[] res = api.getDataSince(21, dt);
		assertEquals(res.length, 3);
		assertEquals((int) res[0].getReading(), 32);
		assertEquals((int) res[1].getReading(), 29);
	}
	
	@Test
	public void testGetDataBetween() {
		DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt1 = null;
		Date dt2 = null;
		try {
			dt1 = ft.parse("2015-04-08 21:12:00");
			dt2 = ft.parse("2015-04-08 21:21:00");
		} catch(Exception e) {
			fail();
		}
		SensorData[] res = api.getDataBetween(21, dt1, dt2);
		assertEquals(res.length, 3);
		assertEquals((int) res[0].getReading(), 30);
		assertEquals((int) res[2].getReading(), 32);
	}
	*/
}
