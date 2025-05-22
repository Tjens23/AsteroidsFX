package dk.sdu.mmmi.cbse.common.asteroids;

    import dk.sdu.mmmi.cbse.common.data.Entity;
    import dk.sdu.mmmi.cbse.common.services.IEntityCircleCollision;
    import dk.sdu.mmmi.cbse.common.services.IEntityHealthPoints;

    /**
     * Asteroid entity with size information
     * @author corfixen
     */
    public class Asteroid extends Entity implements IEntityCircleCollision, IEntityHealthPoints {

        private float speed = 1;
        private int healthPoints;
        private double boundingCircleRadius;
        private boolean scoreProcessed = false;
        public enum Size {
            SMALL(1),
            MEDIUM(20),
            LARGE(30);

            private int size;

            Size(int size) {
                this.size = size;
            }

            public int getSize() {
                return size;
            }

            public int setSize(int size) {
                return this.size = size;
            }
        }

        public Asteroid() {
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        @Override
        public double getBoundingCircleRadius() {
            return boundingCircleRadius;
        }

        public void setBoundingCircleRadius(double newBoundingCircleRadius) {
            this.boundingCircleRadius = newBoundingCircleRadius;
        }

        @Override
        public int getHealthPoints() {
            return this.healthPoints;
        }

        public void setHealthPoints(int newHealthPoints) {
            this.healthPoints = newHealthPoints;
        }

        @Override
        public void takeDamage(int damagePoints) {
            this.healthPoints -= damagePoints;
        }

        public boolean isScoreProcessed() {
            return this.scoreProcessed;
        }

        public void setScoreProcessed(boolean scoreProcessed) {
            this.scoreProcessed = scoreProcessed;
        }
    }