/**
 * @author      Nicholas Eng
 * @since       2021-2-25
 */

public class ComplexNumber {

   	private double a;
   	private double b;
		public static double THRESHOLD = .000000000001; //Variable to check if a number is zero
    public static ComplexNumber C_ONE = new ComplexNumber(1,0);
    public static ComplexNumber C_IMAGINARY = new ComplexNumber(0,1);
    public static ComplexNumber C_ZERO = new ComplexNumber();


    /**
   	 * The default constructor
     * Creates a new ComplexNumber: 0
   	 */
   	public ComplexNumber(){
          	a = 0;
          	b = 0;
   	}

   	/**
   	 * Creates a new ComplexNumber with both real and imaginary components
   	 * @param a the real component of the complex number
   	 * @param b the imaginary component of the complex number
   	 */
   	public ComplexNumber(double a, double b){
          	this.a = a;
          	this.b = b;
   	}

   	/**
   	 * The "copy constructor"
   	 * Creates a new ComplexNumber from an existing ComplexNumber
   	 * @param c a ComplexNumber
   	 */
   	public ComplexNumber(ComplexNumber c){
          	a = c.getReal();
          	b = c.getImaginary();
   	}

		/* Accessor Methods */

   	/**
   	 * An "accessor" method
   	 * Return the real component of this ComplexNumber
   	 * @return a the private real component of this ComplexNumber
   	 */
   	public double getReal(){
          	return a;
   	}

   	/**
   	 * An "accessor" method
   	 * Return the imaginary component of this ComplexNumber
   	 * @return b the private imaginary component of this ComplexNumber
   	 */
   	public double getImaginary(){
          	return b;
   	}

		/**
   	 * An "accessor" method
   	 * Return the imaginary component of this ComplexNumber
   	 * @return b the private imaginary component of this ComplexNumber
   	 */
   	private ComplexNumber conjugate(){
						ComplexNumber place_holder = new ComplexNumber();
						place_holder.a = this.getReal();
						place_holder.b = this.getImaginary() * -1;
          	return place_holder;
   	}

		/**
		 * An "calculator" method
		 * Return the distnace from the origin
		 * @return magnitude or distance from the origin
		 */
		public double magnitude(){
						return Math.sqrt(this.magnitude2());
		}

		/**
   	 * An "accessor" method
   	 * Compares the CompleXNumbers
		 * @param c a ComplexNumber
   	 * @return an integer (-1, 0, 1)
   	 */

		public int compareTo(ComplexNumber c){
			 double magC = c.magnitude();
			 double magC2 = this.magnitude();

			if(magC2 < magC)
			 		return -1;
			if(magC2 > magC)
	 			 	return 1;

			 return 0;
		 }

		/* Arithmetic Methods */

		/**
   	 * An "calculator" instance method
   	 * Return the sum of an imaginary number to an existing ComplexNumber
		 * @param c a ComplexNumber
   	 * @return value of the combined ComplexNumber
   	 */
   	public ComplexNumber add(ComplexNumber c){
						ComplexNumber place_holder = new ComplexNumber();
          	place_holder.a = c.getReal() + this.getReal();
						place_holder.b = c.getImaginary() + this.getImaginary();

						return place_holder;
   	}

		/**
   	 * An "calculator" static method
   	 * Return the sum of an imaginary number to an existing ComplexNumber
		 * @param c1 a ComplexNumber
		 * @param c2 a ComeplxNumber
   	 * @return value of the combined ComplexNumber
   	 */
   	public static ComplexNumber add(ComplexNumber c1, ComplexNumber c2){
		return c1.add(c2);
   	}

		/**
   	 * An "calculator" instance method
   	 * Return the difference of two ComplexNumbers
		 * @param c a ComplexNumber
   	 * @return value of the combined ComplexNumber
   	 */

   	public ComplexNumber subtract(ComplexNumber c){
		ComplexNumber place_holder = new ComplexNumber();
		place_holder = this.add(c.multiply(-1));	//Multiplies c by -1 and adds it to this
		return place_holder;
   	}

		/**
   	 * An "calculator" static method
   	 * Return the difference of two ComplexNumbers
		 * @param c1 a ComplexNumber
		 * @param c2 a ComeplxNumber
   	 * @return value of the combined ComplexNumber
   	 */
   	public static ComplexNumber subtract(ComplexNumber c1, ComplexNumber c2){
		return c1.subtract(c2);
   	}

		/**
   	 * An "calculator" instance method
   	 * Return the product of two complexNumbers
		 * @param c a ComplexNumber
   	 * @return value of the combined ComplexNumber
   	 */
   	public ComplexNumber multiply(ComplexNumber c){

						ComplexNumber place_holder = new ComplexNumber();

						place_holder.a = this.getReal() * c.getReal() - this.getImaginary() * c.getImaginary();
						place_holder.b = this.getReal() * c.getImaginary() + this.getImaginary() * c.getReal();

						return place_holder;
   	}

		/**
   	 * An "calculator" static method
   	 * Return the product of two complexNumbers
		 * @param c1 a ComplexNumber
		 * @param c2 a ComeplxNumber
   	 * @return value of the combined ComplexNumber
   	 */
   	public static ComplexNumber multiply(ComplexNumber c1, ComplexNumber c2){
						return c1.multiply(c2); //Reuses internal methods
   	}

		/**
   	 * An "calculator" overloaded instance method
   	 * Return the product of a complexNumber and a non ComplexNumber
		 * @param d a double
   	 * @return value of the combined ComplexNumber
   	 */
   	public ComplexNumber multiply(double d){
						return new ComplexNumber(this.getReal() * d, this.getImaginary() * d);
   	}

		/**
   	 * An "calculator" method
   	 * Return the dividend of two complexNumbers
		 * @param c a ComplexNumber
   	 * @return value of the combined ComplexNumber
   	 */
   public ComplexNumber divide(ComplexNumber c){
			if (isZero(c.a) && isZero(c.b)){
				throw new ArithmeticException("Error: Division by 0");
			}

			double magC = c.magnitude2();

			return (this.multiply(c.conjugate())).multiply(1/magC);
   }

	 /**
		* An "calculator" static method
		* Return the dividend of two complexNumbers
		* @param c1 a ComplexNumber
		* @param c2 a ComeplxNumber
		* @return value of the combined ComplexNumber
		*/
	public static ComplexNumber divide(ComplexNumber c1, ComplexNumber c2){
		 return c1.divide(c2);
	 }

	 /**
 	 * An "calculator" method
 	 * Return the the ComplexNumber squared
 	 * @return value of the squared ComplexNumber
 	 */
	public ComplexNumber square(){
		return this.power(2);
	}

  /**
  * An "calculator" method
  * Return the the magnitude of a ComplexNumber squared
  * @return value of the squared magnitude of a ComplexNumber
  */
  public double sqrmag() {
      return this.getReal() * this.getReal() + this.getImaginary() * this.getImaginary();
  }

	/**
	* An "calculator" method
	* Return the the ComplexNumber squared
	* @param c a ComplexNumber
	* @return value of the squared ComplexNumber
	*/
 public static ComplexNumber square(ComplexNumber c){
	 return c.square();
 }

 /**
     * A "calculator" method to determine the integral power (both +/-)
     * for a complex number
     * @param exponent
     * @return complex number raised to an integral power
     */
    public ComplexNumber power(int exponent) {
        if ( exponent == 0 ) {
            return C_ONE;
        } else {
            if ( exponent < 0 ) {
                ComplexNumber cn = this.power(-exponent);
                return C_ONE.divide(cn);
            } else {
                ComplexNumber powerResult = C_ONE;
                for (int i = exponent; i > 0; i--) {
                    powerResult = powerResult.multiply(this);
                }
                return powerResult;
            }
        }
    }

    /**
   * A "calculator" method to displayed a rounded complex value
   * @param decimals
   * @return formatted String
   */
  public String round(int decimals) {
      if ( decimals < 0 ) { decimals = 0; }

      double scalerFactor = Math.pow(10,decimals);
      StringBuilder strB = new StringBuilder();

      if (isZero(this.a) && isZero(this.b)) {
          strB.append("0 + 0i");
      } else if (isZero(this.a) && !isZero(this.b)) {
          if (this.b > 0) {
              strB.append("0 + ");
          } else {
              strB.append("0 - ");
          }
          strB.append(Math.abs(Math.round(this.b * scalerFactor + 0.5)/scalerFactor) + "i");
      } else if (!isZero(this.a) && isZero(this.b)) {
          strB.append(Math.round(this.a * scalerFactor + 0.5)/scalerFactor + " + 0i");
      } else {
          strB.append(Math.round(this.a * scalerFactor + 0.5)/scalerFactor);

          if (this.b > 0) {
              strB.append(" + ");
          } else {
              strB.append(" - ");
          }
          strB.append(Math.abs(Math.round(this.b * scalerFactor + 0.5)/scalerFactor) + "i");
      }

      return strB.toString();
  }

    /**
    * An "calculator" method
    * Return the formatted version of a ComplexNumber
		* @return formatted String
    */

    public String toString(){

      StringBuilder strB = new StringBuilder();

	  if (isZero(a) && isZero(b)){
						strB.append("0+0i");
	  			}

	  else if (isZero(a) && !isZero(b)){
		  					strB.append(b + "i");
	 			}

      else if (!isZero(a) && isZero(b)){ //no 0 coefficients i.e. 2i instead of 0+2i, or 2 instead of 2+0i
          					strB.append(a);
      			}
	  else{
							strB.append(a);

			if (b > 0){
					strB.append("+");
				}
				strB.append(b + "i");
	  			}

      return strB.toString(); //StringBuilder method

    }

		/**
   	 * An "helper" function
   	 * Checks to see if the value is zero
		 * @param d a double
   	 * @return true or false
   	 */
   	public static boolean isZero(double d){
						return (Math.abs(d) < THRESHOLD);
   	}

		/**
		 * An "helper" method
		 * Return the distnace from the origin
		 * @return magnitude squared
		 */
		public double magnitude2(){
						return (a*a + b*b);
		}

   	/**
   	 * A tester method
   	 * @param args Runs code in the main method
   	 */
   	public static void main(String[] args) {
						ComplexNumber c1 = new ComplexNumber(1, 4);
						ComplexNumber c2 = new ComplexNumber(5, 1);
						ComplexNumber c4 = new ComplexNumber(0,0);

						ComplexNumber temp = c1.multiply(c2);
						System.out.println("Zero is: "+ c4.toString());
						System.out.println("Product is: "+ temp.toString());
						System.out.println("Conjugate is: "+ temp.conjugate().toString());


          	//System.out.print("Specific Constructor, Accessor. (c1:1-2i r:1 i:-2):");
          	//System.out.println("c1: "+c1.toString());
          	//System.out.print("Copy Constructor, Accessor. (c2:1-2i r:1 i:-2):");
          	//System.out.println("c2:"+c2+" r:"+c2.getReal()+" i:"+c2.getImaginary());
   	}
}
