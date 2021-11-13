package br.com.gmltec.geo;

import java.util.Random;

public class GeoUtils {
	
	public static Coordinate generateRandomCoordinate(Coordinate centralPoint, double max_range) {
		Random random = new Random();
		double distance = random.nextDouble()* (max_range- max_range/3) + max_range/3;
		double bearing = random.nextDouble()* (359 - 1) + 1;
		
		Coordinate coord = calculateNewPosition(centralPoint, distance,bearing);
	    
		return coord;
	}
	
	
	/**
	 * Returns the destination point from a given point, having travelled the given
	 * distance on the given initial bearing.
	 *
	 * @param {number} lat - initial latitude in decimal degrees (eg. 50.123)
	 * @param {number} lon - initial longitude in decimal degrees (e.g. -4.321)
	 * @param {number} distance - Distance travelled (metres).
	 * @param {number} bearing - Initial bearing (in degrees from north).
	 * @returns {array} destination point as [latitude,longitude] (e.g. [50.123,
	 *          -4.321])
	 *
	 * @example var p = destinationPoint(51.4778, -0.0015, 7794, 300.7); //
	 *          51.5135°N, 000.0983°W
	 */
	private static Coordinate calculateNewPosition(Coordinate start_pos, double distance_mt_horiz,double bearing_dg_from_north) {
		/**
		 * https://stackoverflow.com/questions/19352921/how-to-use-direction-angle-and-speed-to-calculate-next-times-latitude-and-longi
		 */
		// var radius = 6371e3; // (Mean) radius of earth
		double radius = 6371e3; // (Mean) radius of earth

		// sinφ2 = sinφ1·cosδ + cosφ1·sinδ·cosθ
		// tanΔλ = sinθ·sinδ·cosφ1 / cosδ−sinφ1·sinφ2
		// see mathforum.org/library/drmath/view/52049.html for derivation

		// var φ1 = toRadians(Number(lat));
		double lat_1_radians = Math.toRadians(start_pos.getLatitude());

		// var sinφ1 = Math.sin(φ1), cosφ1 = Math.cos(φ1);
		double var_sin_lat_1 = Math.sin(lat_1_radians);
		double var_cos_lat_1 = Math.cos(lat_1_radians);

		// var λ1 = toRadians(Number(lon));
		double long_1_radians = Math.toRadians(start_pos.getLongitude());

		// var δ = Number(distance) / radius; // angular distance in radians
		double ang_dist_radians = distance_mt_horiz / radius;

		// var sinδ = Math.sin(δ), cosδ = Math.cos(δ);
		double var_sin_ang_dist = Math.sin(ang_dist_radians);
		double var_cos_ang_dist = Math.cos(ang_dist_radians);

		// var θ = toRadians(Number(bearing));
		double bearing_radians = Math.toRadians(bearing_dg_from_north);

		// var sinθ = Math.sin(θ), cosθ = Math.cos(θ);
		double var_sin_bear_radians = Math.sin(bearing_radians);
		double var_cos_bear_radians = Math.cos(bearing_radians);

		// var sinφ2 = sinφ1*cosδ + cosφ1*sinδ*cosθ;
		double var_sin_lat2 = var_sin_lat_1 * var_cos_ang_dist
				+ var_cos_lat_1 * var_sin_ang_dist * var_cos_bear_radians;

		// var φ2 = Math.asin(sinφ2);
		double lat2 = Math.asin(var_sin_lat2);

		// var y = sinθ * sinδ * cosφ1;
		double y = var_sin_bear_radians * var_sin_ang_dist * var_cos_lat_1;

		// var x = cosδ - sinφ1 * sinφ2;
		double x = var_cos_ang_dist - var_sin_lat_1 * var_sin_lat2;

		// var λ2 = λ1 + Math.atan2(y, x);
		double long2 = long_1_radians + Math.atan2(y, x);

		// return [toDegrees(φ2), (toDegrees(λ2)+540)%360-180]; // normalise to
		// −180..+180°

		Coordinate result = new Coordinate(Math.toDegrees(lat2), ((Math.toDegrees(long2) + 540) % 360 - 180),
				0);

		return result;
	}

}
