var maze = function(cols, col_width) {
    this.wall_line_width = 5
    this.wall_line_color = '#042124'

    this.lock_image_file = '/lock.png'
    this.lock_image = new Image()
    this.lock_image.src = this.lock_image_file

    this.key_image_file = '/key.png'
    this.key_image = new Image()
    this.key_image.src = this.key_image_file

    this.cols = cols
    this.rows = Math.floor(this.cols / 1.77777777778) - 1

    this.col_width = col_width
    this.cell_width = this.col_width - this.wall_line_width

    this.key_coordinates = []
    this.exit_coordinates = [this.cols - 1, this.rows - 1]

    this.horizontal_walls = []
    this.vertical_walls = []

    this.generate = function() {
        var n = this.rows * this.cols - 1

        if (n < 0) {
            alert('illegal maze dimensions')
            return
        }

        for (var j = 0; j < this.rows + 1; j++) {
            this.horizontal_walls[j] = []
        }

        for (var j = 0; j < this.rows + 1; j++) {
            this.vertical_walls[j] = []
        }

        here = [
            Math.floor(Math.random() * this.rows),
            Math.floor(Math.random() * this.cols),
        ]
        path = [here]

        unvisited = []
        for (var j = 0; j < this.rows + 2; j++) {
            unvisited[j] = []

            for (var k = 0; k < this.cols + 1; k++) {
                unvisited[j].push(
                    j > 0 &&
                        j < this.rows + 1 &&
                        k > 0 &&
                        (j != here[0] + 1 || k != here[1] + 1)
                )
            }
        }

        while (0 < n) {
            var potential = [
                [here[0] + 1, here[1]],
                [here[0], here[1] + 1],
                [here[0] - 1, here[1]],
                [here[0], here[1] - 1],
            ]

            var neighbors = []

            for (var j = 0; j < 4; j++) {
                if (unvisited[potential[j][0] + 1][potential[j][1] + 1]) {
                    neighbors.push(potential[j])
                }
            }

            if (neighbors.length) {
                n = n - 1
                next = neighbors[Math.floor(Math.random() * neighbors.length)]
                unvisited[next[0] + 1][next[1] + 1] = false
                if (next[0] == here[0])
                    this.horizontal_walls[next[0]][
                        (next[1] + here[1] - 1) / 2
                    ] = true
                else
                    this.vertical_walls[(next[0] + here[0] - 1) / 2][
                        next[1]
                    ] = true
                path.push((here = next))
            } else here = path.pop()
        }
        for (i = 0; i < this.rows - 1; i++) {
            for (j = 0; j < this.cols; j++) {
                if (!Game.maze.vertical_walls[i][j]) {
                    Game.maze.vertical_walls[i][j] = false
                }
               
            }
        }

        for (i = 0; i < this.rows; i++) {
            for (j = 0; j < this.cols - 1; j++) {
                if (!Game.maze.horizontal_walls[i][j]) {
                    Game.maze.horizontal_walls[i][j] = false
                }
            }
        }

        // coordinates for key
        var max = this.rows - 1
        var min = Math.floor(this.rows / 2)
        var range = max - min + 1 + min
        this.key_coordinates[0] = Math.floor(Math.random() * range)
        this.key_coordinates[1] = Math.floor(Math.random() * range)
    }

    this.getTotalWidth = function() {
        return this.col_width * this.cols + this.wall_line_width / 4
    }

    this.getTotalHeight = function() {
        return this.col_width * this.rows + this.wall_line_width / 4
    }
    this.render = function() {
        // Exit
        Game.ctx.drawImage(
            this.lock_image,
            0,
            0,
           50,
            50,
            this.exit_coordinates[0] * Game.maze.col_width +
                Game.maze.col_width / 4,
            this.exit_coordinates[1] * Game.maze.col_width +
                Game.maze.col_width / 4,
            Game.maze.col_width * 0.5,
            Game.maze.col_width * 0.5
        )

        // Key
        if (!Game.player.has_key) {
            Game.ctx.drawImage(
                this.key_image,
                0,
                0,
                44,
                44,
                this.key_coordinates[0] * Game.maze.col_width +
                    Game.maze.col_width / 4,
                this.key_coordinates[1] * Game.maze.col_width +
                    Game.maze.col_width / 4,
                Game.maze.col_width * 0.5,
                Game.maze.col_width * 0.5
            )
        }

        // Map Walls
        for (var i = 0; i < Game.maze.rows; i++) {
            for (var j = 0; j < Game.maze.cols; j++) {
                if (j < Game.maze.cols - 1) {
                    if (!Game.maze.horizontal_walls[i][j]) {
                        Game.ctx.beginPath()
                        Game.ctx.strokeStyle = Game.maze.wall_line_color
                        Game.ctx.lineWidth = Game.maze.wall_line_width

                        Game.ctx.moveTo(
                            j * Game.maze.col_width + Game.maze.col_width,
                            Game.maze.col_width * i
                        )
                        Game.ctx.lineTo(
                            j * Game.maze.col_width + Game.maze.col_width,
                            Game.maze.col_width * i + Game.maze.col_width
                        )
                        Game.ctx.closePath()
                        Game.ctx.stroke()
                    }
                }
                if (i < Game.maze.rows - 1) {
                    if (!Game.maze.vertical_walls[i][j]) {
                        Game.ctx.beginPath()
                        Game.ctx.strokeStyle = Game.maze.wall_line_color
                        Game.ctx.lineWidth = Game.maze.wall_line_width
                        Game.ctx.moveTo(
                            j * Game.maze.col_width -
                                Game.maze.wall_line_width / 2,
                            i * Game.maze.col_width + Game.maze.col_width
                        )
                        Game.ctx.lineTo(
                            j * Game.maze.col_width +
                                Game.maze.col_width +
                                Game.maze.wall_line_width / 2,
                            i * Game.maze.col_width + Game.maze.col_width
                        )
                        Game.ctx.closePath()
                        Game.ctx.stroke()
                    }
                }
            }
        }
    }
}