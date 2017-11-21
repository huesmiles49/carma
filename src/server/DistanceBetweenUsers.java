package server;
public class DistanceBetweenUsers {
	
	
	public DistanceBetweenUsers(){
		
	}

	/*
	 *	Starting point: lng1, lat1
	 *	End point: 		lng2, lat2	
	 *
	 */
	
	protected double distanceInMiles(String lng1, String lat1, String lng2, String lat2){
		double a, c, d;
		final int R = 6371; // Radius of the earth
		double distanceInMiles;
		
		//corrdinates of one user
		double lngStart = Double.parseDouble(lng1);
		double latStart = Double.parseDouble(lat1);
		
		//corrdinates of another user		
		double lngEnd = Double.parseDouble(lng2);
		double latEnd = Double.parseDouble(lat2);
		
		double distanceLng = Math.toRadians(lngEnd - lngStart); 
		double distanceLat = Math.toRadians(latEnd - latStart); 
		
		
		/* 	Note: φ is latitude, λ is longitude, R is earth’s radius
		 * 
		 *	Haversine formula:	
		 *		a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
		 *		c = 2 ⋅ atan2( √a, √(1−a) )
		 * 		d = R ⋅ c
		 */
		
		a = Math.sin(distanceLat/2) * Math.sin(distanceLat/2) +							//sin²(Δφ/2)
			    Math.cos(Math.toRadians(latStart)) * Math.cos(Math.toRadians(latEnd)) * 	//cos φ1 ⋅ cos φ2
			    Math.sin(distanceLng/2) * Math.sin(distanceLng/2);						//sin²(Δλ/2)
		
		c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));								//2 ⋅ atan2( √a, √(1−a) )
		
		d = R * c * 1000;
		
		
		distanceInMiles = d/1609.344;
		
		distanceInMiles = Math.pow(distanceInMiles, 2) + Math.pow(distanceInMiles, 2);

	    return Math.sqrt(distanceInMiles);
	}
	
}