var player = function(_x, _y) {
    this.x = _x
    this.y = _y

    this.colors = ['#00D100']

    this.has_key = true

    this.speed_counter = 0

    this.render = function() {
        Game.ctx.beginPath()
        Game.ctx.fillStyle = this.colors[0]
        Game.ctx.arc(
            this.x * Game.maze.col_width + Game.maze.col_width / 2,
            this.y * Game.maze.col_width + Game.maze.col_width / 2,
            Game.maze.col_width * 0.25,
            0,
            Math.PI * 2,
            true
        )
        Game.ctx.fill()

        Game.ctx.beginPath()
        Game.ctx.fillStyle = this.colors[2]
        Game.ctx.arc(
            this.x * Game.maze.col_width + Game.maze.col_width / 2,
            this.y * Game.maze.col_width + Game.maze.col_width / 2,
            Game.maze.col_width * 0.2,
            0,
            Math.PI * 2,
            true
        )
        Game.ctx.fill()

        Game.ctx.beginPath()
        Game.ctx.fillStyle = this.colors[1]
        Game.ctx.arc(
            this.x * Game.maze.col_width + Game.maze.col_width / 2,
            this.y * Game.maze.col_width + Game.maze.col_width / 2,
            Game.maze.col_width * 0.15,
            0,
            Math.PI * 2,
            true
        )
        Game.ctx.fill()

        Game.ctx.beginPath()
        Game.ctx.fillStyle = '#ffffff'
        Game.ctx.arc(
            this.x * Game.maze.col_width + Game.maze.col_width / 2,
            this.y * Game.maze.col_width + Game.maze.col_width * 0.45,
            Game.maze.col_width * 0.05,
            0,
            Math.PI * 2,
            true
        )
        Game.ctx.fill()

        Game.ctx.beginPath()
        Game.ctx.fillStyle = this.colors[0]
        Game.ctx.arc(
            this.x * Game.maze.col_width + Game.maze.col_width / 2,
            this.y * Game.maze.col_width + Game.maze.col_width * 0.45,
            Game.maze.col_width * 0.025,
            0,
            Math.PI * 2,
            true
        )
        Game.ctx.fill()

        Game.ctx.beginPath()
        Game.ctx.lineWidth = 1
        Game.ctx.arc(
            this.x * Game.maze.col_width + Game.maze.col_width / 2,
            this.y * Game.maze.col_width + Game.maze.col_width * 0.55,
            Game.maze.col_width * 0.05,
            0,
            Math.PI,
            false
        )
        Game.ctx.stroke()
    }

    this.move = function(direction) {
        if (this.speed_counter < 5) {
            this.speed_counter++
        } else {
            this.speed_counter = 0
            switch (direction) {
                case 'up':
                    if (this.canMoveTo(this.x, this.y - 1)) {
                        this.y--
                    }
                    break

                case 'down':
                    if (this.canMoveTo(this.x, this.y + 1)) {
                        this.y++
                    }
                    break

                case 'left':
                    if (this.canMoveTo(this.x - 1, this.y)) {
                        this.x--
                    }
                    break

                case 'right':
                    if (this.canMoveTo(this.x + 1, this.y)) {
                        this.x++
                    }
                    break
            }

            this.interactions()
        }
    }

    this.interactions = function() {
        if (
            this.x == Game.maze.key_coordinates[0] &&
            this.y == Game.maze.key_coordinates[1]
        ) {
            this.has_key = true
        }
        if (
            this.x == Game.maze.exit_coordinates[0] &&
            this.y == Game.maze.exit_coordinates[1]
        ) {
            if (!this.has_key) {
                console.log('Necesitas la llave para salir')
            } else {
                Game.paused = true
                cancelAnimationFrame(Game.reqanimationreference)
            }
        }
    }

    this.canMoveTo = function(target_x, target_y) {

        if (
            target_x < 0 ||
            target_y < 0 ||
            target_y > Game.maze.rows - 1 ||
            target_x > Game.maze.cols - 1
        ) {
            return false
        } else {
            var result
            if (target_x > this.x) {
                return Game.maze.horizontal_walls[this.y][target_x - 1]
            }

            if (target_x < this.x) {
                return Game.maze.horizontal_walls[this.y][target_x]
            }

            if (target_y > this.y) {
                return Game.maze.vertical_walls[this.y][target_x]
            }

            if (target_y < this.y) {
                return Game.maze.vertical_walls[this.y - 1][target_x]
            }
        }
    }
}
