package de.hft_stuttgart.spirit.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

import android.content.Context;

public class ORBpruefung implements Runnable {
	Mat szene;
	Mat vergleich;
	int keypoints;
	OrbCallback oc;
	FeatureDetector detector;
	DescriptorExtractor descriptor;
	DescriptorMatcher matcher;
	Context c;
	String name;

	public ORBpruefung(Mat szene, Mat vergleich, int keypoints, OrbCallback oc,
			Context c, String name) {
		this.szene = szene;
		this.vergleich = vergleich;
		if (keypoints < 250)
			keypoints = 250;
		this.keypoints = keypoints;
		this.oc = oc;
		this.c = c;
		this.name = name;
	}

	@Override
	public void run() {
		// skalieren
		scaleMat(szene);
		scaleMat(vergleich);
		// System.out.println(szene);
		// System.out.println(vergleich);
		// prüfen
		detector = FeatureDetector.create(FeatureDetector.ORB);

		try {
			File outputDir = c.getCacheDir();
			File outputFile = File.createTempFile("orbDetectorParams", ".YAML",
					outputDir);
			writeToFile(
					outputFile,
					"%YAML:1.0\nscaleFactor: 1.2\nnLevels: 18\nfirstLevel: 0 \nedgeThreshold: 31\npatchSize: 31\nWTA_K: 2\nscoreType: 1\nnFeatures: "
							+ keypoints + "\n");

			detector.read(outputFile.getPath());
		} catch (Exception e) {

		}
		descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		matcher = DescriptorMatcher
				.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
		Mat desc1 = new Mat();
		MatOfKeyPoint keyp1 = new MatOfKeyPoint();
		Mat desc2 = new Mat();
		MatOfKeyPoint keyp2 = new MatOfKeyPoint();
		detector.detect(szene, keyp1);
		descriptor.compute(szene, keyp1, desc1);
		detector.detect(vergleich, keyp2);
		descriptor.compute(vergleich, keyp2, desc2);
		MatOfDMatch mtch = doMatchAndFilter(desc1, desc2, keyp1, keyp2);

		List<DMatch> matchesList = mtch.toList();
		List<KeyPoint> kpList = keyp1.toList();

		double max_dist = 0;
		double min_dist = 100;
		double avg_dist = 0;
		int row_count = matchesList.size();
		double minx = 1280;
		double maxx = 0;
		double miny = 720;
		double maxy = 0;
		double gesamt = 0;
		for (int i = 0; i < matchesList.size(); i++) {
			double dist = matchesList.get(i).distance;
			if (dist < min_dist) {
				min_dist = dist;
			}
			if (dist > max_dist) {
				max_dist = dist;
			}

			if (kpList.get(matchesList.get(i).queryIdx).pt.x < minx)
				minx = kpList.get(matchesList.get(i).queryIdx).pt.x;
			if (kpList.get(matchesList.get(i).queryIdx).pt.x > maxx)
				maxx = kpList.get(matchesList.get(i).queryIdx).pt.x;
			if (kpList.get(matchesList.get(i).queryIdx).pt.y < miny)
				miny = kpList.get(matchesList.get(i).queryIdx).pt.y;
			if (kpList.get(matchesList.get(i).queryIdx).pt.y > maxy)
				maxy = kpList.get(matchesList.get(i).queryIdx).pt.y;
			gesamt += dist;
		}
		avg_dist = gesamt / row_count;

		// debug
		// Highgui.imwrite("/storage/sdcard1/ORBdebugSzene.jpg", szene);
		// Highgui.imwrite("/storage/sdcard1/ORBdebugVergleich.jpg", vergleich);

		// ergebnis mitteilen
		System.out.println("Created OrbResult: min:"+min_dist+" max:"+max_dist+" avg:"+avg_dist+" name:"+name);

		if (oc != null) {
			oc.onOrbResult(new orbResult(min_dist, max_dist, avg_dist, name));
			
		}
		
		// aufräumen
		szene.release();
		vergleich.release();
		desc1.release();
		keyp1.release();
		desc2.release();
		keyp2.release();
		mtch.release();
	}

	private void scaleMat(Mat m) {
		float scale = 1f;
		float targetSize = 400f;
		if (m.width() > m.height()) {
			scale = targetSize / m.width();
		} else {
			scale = targetSize / m.height();
		}
		Imgproc.resize(m, m,
				new Size((int) (m.width() * scale), (int) (m.height() * scale)));
	}

	private void writeToFile(File file, String data) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
		outputStreamWriter.write(data);
		outputStreamWriter.close();
		stream.close();
	}

	public MatOfDMatch doMatchAndFilter(Mat desc1, Mat desc2,
			MatOfKeyPoint kp1, MatOfKeyPoint kp2) {

		// symmetric matches

		MatOfDMatch matches12 = new MatOfDMatch();
		MatOfDMatch matches21 = new MatOfDMatch();

		// System.out.println(desc1);
		// System.out.println(desc2);

		matcher.match(desc1, desc2, matches12);
		if (desc1.rows() == 0) {
			matcher.match(desc1, desc2, matches21);
		} else {
			matcher.match(desc2, desc1, matches21);

		}
		List<DMatch> matchesList12 = matches12.toList();
		List<DMatch> matchesList21 = matches21.toList();
		List<DMatch> matches = new ArrayList<DMatch>();

		List<KeyPoint> keypoints1 = kp1.toList();
		List<KeyPoint> keypoints2 = kp2.toList();

		for (DMatch match1 : matchesList12) {
			for (DMatch match2 : matchesList21) {
				if (match1.queryIdx < keypoints2.size()) {
					if (match2.queryIdx < keypoints1.size()) {
						if (match1.trainIdx < keypoints1.size()) {
							if (match2.trainIdx < keypoints2.size()) {
								if (keypoints2.get(match1.queryIdx) == keypoints2
										.get(match2.trainIdx)) {
									if (keypoints1.get(match1.trainIdx) == keypoints1
											.get(match2.queryIdx)) {
										matches.add(match1);
									}
								}
							}
						}
					}
				}
			}
		}

		matches = filterDistance(matches);

		matches12.release();
		matches21.release();

		return filterRansac(matches, kp1, kp2, 2);
	}

	private List<DMatch> filterDistance(List<DMatch> matches) {
		// distance filtern
		float min = Float.MAX_VALUE;
		float max = Float.MIN_NORMAL;
		float sum = 0;

		for (DMatch match : matches) {
			if (match.distance < min) {
				min = match.distance;
			}
			if (match.distance > max) {
				max = match.distance;
			}
			sum += match.distance;
		}
		float avg = sum / matches.size();
		// System.out.println("" + matches.size()
		// + " Matches, Durchschnitt Distance: " + avg + " Min: " + min
		// + " Max: " + max);

		for (int i = matches.size() - 1; i >= 0; i--) {
			if (matches.get(i).distance > (2 * min)) {
				matches.remove(i);
			}
		}

		return matches;
	}

	private MatOfDMatch filterRansac(List<DMatch> matches, MatOfKeyPoint kp1,
			MatOfKeyPoint kp2, int iterations) {
		KeyPoint kps1[] = kp1.toArray();
		KeyPoint kps2[] = kp2.toArray();

		ArrayList<Point> p1 = new ArrayList<Point>();
		ArrayList<Point> p2 = new ArrayList<Point>();

		// liste der gematchten keypoints
		for (DMatch m : matches) {
			p1.add(kps1[m.queryIdx].pt);
			p2.add(kps1[m.trainIdx].pt);
		}

		MatOfPoint2f src = new MatOfPoint2f();
		src.fromList(p1);
		MatOfPoint2f dst = new MatOfPoint2f();
		dst.fromList(p2);

		// System.out.println("P1: " + p1.size() + " P2: " + p2.size()
		// + " Matches: " + matches.size());
		if (p1.size() > 15 && p2.size() > 15 && iterations > 0) {
			Mat mask = new Mat();
			Mat h = Calib3d.findHomography(src, dst, Calib3d.RANSAC, 10, mask);
			// System.out.println("Ransac: " + h + " Mask: " + mask);
			ArrayList<DMatch> hm = new ArrayList<DMatch>();
			for (int i = 0; i < mask.rows(); i++) {
				if (mask.get(i, 0)[0] == 1.0) {
					hm.add(matches.get(i));
				}
			}
			mask.release();
			h.release();
			return filterRansac(hm, kp1, kp2, iterations - 1);
		}

		MatOfDMatch filteredMatches = new MatOfDMatch();
		filteredMatches.fromList(matches);
		src.release();
		dst.release();
		return filteredMatches;
	}
}
