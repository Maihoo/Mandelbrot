package gameClasses;

public class Complex {
    double re;
    double im;

  
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    public double abs() {
        return Math.hypot(re, im);
    }
    
    public Complex plus(Complex b) {
    	Complex a = this;
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    public Complex times(Complex b) {
    	Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    public double re() { return re; }
    public double im() { return im; }
    
    public void setComplex(double x, double y) {
    	re = x;
    	im = y;
    }
    
    public boolean equals(Complex input) {
    	Complex a = this;
    	if(input.re == a.re && input.im == a.im) return true;
    	return false;
    }

    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }
}