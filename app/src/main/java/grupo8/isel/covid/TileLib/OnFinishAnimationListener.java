package grupo8.isel.covid.TileLib;

/**
 * Interface to implemented by listeners of animations.
 * @author Palex
 */
public interface OnFinishAnimationListener {

	/**
	 * When animations are finished
     * @see Animator#triggerOnFinishAnimations
	 * @param tag Tag provided in triggerOnFinishAnimations()
	 */
	void onFinish(Object tag);
}