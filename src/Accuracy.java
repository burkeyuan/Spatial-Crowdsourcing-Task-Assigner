package igeocrowd;

public class Accuracy {
	private double Dacc_A;
	private double Dacc_B;
	private double Dacc_C;
	private double Dacc_D;
	private int Iacc_A;
	private int Iacc_B;
	private int Iacc_C;
	private int Iacc_D;
	public Accuracy(double accA, double accB, double accC, double accD) {
		Dacc_A = accA;
		Dacc_B = accB;
		Dacc_C = accC;
		Dacc_D = accD;
	}
	
	public Accuracy(int accA, int accB, int accC, int accD) {
		Iacc_A = accA;
		Iacc_B = accB;
		Iacc_C = accC;
		Iacc_D = accD;
	}
	
	public double getDaccA() {
		return Dacc_A;
	}
	public double getDaccB() {
		return Dacc_B;
	}
	public double getDaccC() {
		return Dacc_C;
	}
	public double getDaccD() {
		return Dacc_D;
	}
	public int getIaccA() {
		return Iacc_A;
	}
	public int getIaccB() {
		return Iacc_B;
	}
	public int getIaccC() {
		return Iacc_C;
	}
	public int getIaccD() {
		return Iacc_D;
	}
}